package com.ynw.system.controller;

import com.ynw.system.entity.*;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.AcUserImageService;
import com.ynw.system.service.AcUserService;
import com.ynw.system.service.NotifyService;
import com.ynw.system.service.PrivacyService;
import com.ynw.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/11
 */
@RestController
@RequestMapping("/ynw-ms/acUser")
@Api(tags = "APP用户接口",description = "APP用户接口")
public class AcUserController {

    @Autowired
    private AcUserService acUserService;
    @Autowired
    private AcUserImageService acUserImageService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private PrivacyService privacyService;
    @Value("${FilePath}")
    private String FilePath;
    @Autowired
    private GenerateName generateName;



    /**
     *  根据传入的数字插入等量机器人
     * @param robotNumber 插入数量
     * @return
     */
    @PostMapping("/roboticize")
    @ApiOperation(value ="根据传入的数字插入等量机器人",notes="传入：插入数量（robotNumber）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="robotNumber",value = "插入数量",required = true,dataType = "String",paramType = "query", example = "1")
    })
    public ResponseEntity<Result> roboticize(Integer robotNumber) {
        if (null != robotNumber && robotNumber > 0) {
            Integer no = Integer.valueOf(acUserService.findMaxNo().getNo());
            Integer i = 0;
            List<AcUser> acUserList = new ArrayList<>();
            for (;i < robotNumber; i++) {
                no++;
                acUserList.add(generateName.getAddress(no));
            }
            if (acUserService.insertByBatch(acUserList) != acUserList.size())
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            for (AcUser user: acUserList
                 ) {
                acUserService.register(user);
            }
            return ResultUtil.successResponse();
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  下载昨天的活跃用户信息
     * @param response
     */
    @GetMapping("/activeYesterday")
    @ApiOperation(value ="下载昨天的活跃用户信息")
    public void activeYesterday(HttpServletResponse response) {
        DataToExcel.downloadExcel(FilePath + "/system/"+ DateUtils.dateString(DateUtils.getBeginDayOfYesterday()) +"-2.xls", response);
    }

    /**
     * 下载昨日注册用户信息
     * @param response
     */
    @GetMapping("/yesterdayRegistered")
    @ApiOperation(value ="下载昨日注册用户信息")
    public void yesterdayRegistered(HttpServletResponse response) {
        DataToExcel.downloadExcel(FilePath + "/system/"+ DateUtils.dateString(DateUtils.getBeginDayOfYesterday()) +"-1.xls", response);
    }

    /**
     * 下载昨日用户统计信息
     * @param response
     */
    @GetMapping("/yesterdayStatistics")
    @ApiOperation(value ="下载昨日用户统计信息")
    public void yesterdayStatistics(HttpServletResponse response) {
        DataToExcel.downloadExcel(FilePath + "/system/"+ DateUtils.dateString(DateUtils.getBeginDayOfYesterday()) +"-3.xls", response);
    }

    /**
     *  条件获取用户数据
     * @param acUser
     * @param provinceId 省份编号
     * @param nowPage 当前页
     * @return
     */
    @ApiOperation(value ="条件获取用户数据",notes="传入：页码（nowPage必传），昵称（nickname），用户手机号码（phoneNumber），用户编号（no），" +
            "性别（sex），省份编号（provinceId），" +
            "用户当前所在城市ID（bdCityId），用户状态（status），是否存在未审核图片（imageStatus），实名认证状态（idVerifyStatus）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="provinceId",value = "省份编号",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name="nowPage",value = "当前页",required = true, dataType = "Long",paramType = "query", example = "1")
    })
    @PostMapping("/conditionQueryAcUser")
    public ResponseEntity<Result> conditionQueryAcUser(AcUser acUser, String provinceId, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            if (StringUtils.isEmpty(provinceId)) {
                provinceId = null;
            }
            if (StringUtils.isEmpty(acUser.getBdCityId())) {
                acUser.setBdCityId(null);
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<AcUser> acUserList = acUserService.conditionQueryAcUser(acUser, provinceId, start, pageSize);
            if (acUserList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                if (StringUtils.isNotEmpty(url)) {
                    for (AcUser user :acUserList
                    ) {
                        user.setHeadImageUrl(url+user.getHeadImageUrl());
                    }
                }
                return ResultUtil.successResponse(acUserList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件获取数据总数
     * @param acUser
     * @param provinceId 省份编号
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value ="条件获取数据总数",notes="传入：昵称（nickname），用户手机号码（phoneNumber），用户编号（no），" +
            "性别（sex），省份编号（provinceId），" +
            "用户当前所在城市ID（bdCityId），用户状态（status），是否存在未审核图片（imageStatus），实名认证状态（idVerifyStatus）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="provinceId",value = "省份编号",dataType = "String",paramType = "query")
    })
    public ResponseEntity<Result> count(AcUser acUser, String provinceId) {
        if (StringUtils.isEmpty(provinceId)) {
            provinceId = null;
        }
        if (StringUtils.isEmpty(acUser.getBdCityId())) {
            acUser.setBdCityId(null);
        }
        return ResultUtil.successResponse(acUserService.count(acUser, provinceId));
    }

    /**
     *  根据编号查询单条数据
     * @param acUser
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value ="根据编号查询单条数据",notes="传入：编号（acUserId)")
    public ResponseEntity<Result> findById(AcUser acUser) {
        if (StringUtils.isNotEmpty(acUser.getAcUserId())) {
            AcUser acUser1 = acUserService.findById(acUser);
            if (null != acUser1) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                if (StringUtils.isNotEmpty(url)) {
                    acUser1.setHeadImageUrl(url + acUser1.getHeadImageUrl());
                    acUser1.setIdImageBackUrl(url + acUser1.getIdImageBackUrl());
                    acUser1.setIdImageFrontUrl(url + acUser1.getIdImageFrontUrl());
                }
                return ResultUtil.successResponse(acUser1);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改用户状态
     * @param acUser
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value ="修改用户状态",notes="传入：编号（acUserId),状态（status）")
    public ResponseEntity<Result> update(AcUser acUser) {
        if (StringUtils.isNotEmpty(acUser.getAcUserId()) && null != acUser.getStatus()) {
            if (acUserService.update(acUser) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改用户身份证状态
     * @param user
     * @return
     */
    @PostMapping("/updateIDByUserId")
    @ApiOperation(value ="修改用户身份证状态及给用户推送审核不通过原因",notes="传入：编号（acUserId)必传,身份证状态（idVerifyStatus）必传，推送内容（content）")
    @ApiImplicitParam(name="content",value = "推送内容",dataType = "String",paramType = "query")
    public ResponseEntity<Result> updateIDByUserId(AcUser user, String content) {
        if (StringUtils.isEmpty(user.getAcUserId()) || null == user.getIdVerifyStatus())
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        AcUser acUser = acUserService.findById(user);
        if (null == acUser)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        if (acUser.getIdVerifyStatus() == user.getIdVerifyStatus())
            return ResultUtil.errorResponse(ResultEnums.THE_SAME_STATE);
        if (acUserService.updateIDByUserId(user, content) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  根据编号将头像替换成系统头像
     * @param acUser
     * @return
     */
    @PostMapping("/replaceHeadById")
    @ApiOperation(value ="根据编号将头像替换成系统头像",notes="传入：编号（acUserId)必传")
    public ResponseEntity<Result> replaceHeadById(AcUser acUser) {
        if (StringUtils.isNotEmpty(acUser.getAcUserId())) {
            AcUser user = acUserService.findById(acUser);
            if (null == user)
                return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
            if (user.getSex() == 1) {

            }
            if (acUserService.replaceHeadById(acUser.getAcUserId()) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  获取用户相片
     * @param acUserId
     * @return
     */
    @PostMapping("/findAcUserImageByUserId")
    @ApiOperation(value ="获取用户相片",notes="传入：编号（acUserId)必传")
    @ApiImplicitParam(name="acUserId",value = "编号",required = true, dataType = "String",paramType = "query")
    public ResponseEntity<Result> findAcUserImageByUserId(String acUserId) {
        if (StringUtils.isEmpty(acUserId)) {
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        List<AcUserImage> imageList = acUserImageService.selectList(new AcUserImage(acUserId));
        if (imageList.size() > 0) {
            String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
            if (StringUtils.isNotEmpty(url)) {
                for (AcUserImage image: imageList
                ) {
                    image.setImageUrl(url + image.getImageUrl());
                }
            }
            return ResultUtil.successResponse(imageList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  审核照片
     * @param acUserImage
     * @param content
     * @return
     */
    @PostMapping("/updateAcUserImageById")
    @ApiOperation(value ="审核照片及给用户推送审核不通过原因",notes="传入：编号（acUImageId)必传，推送内容（content）")
    @ApiImplicitParam(name="content",value = "推送内容",dataType = "String",paramType = "query")
    public ResponseEntity<Result> updateAcUserImageById(AcUserImage acUserImage, String content) {
        if (StringUtils.isNotEmpty(acUserImage.getAcUImageId()) && null != acUserImage.getStatus()) {
            return acUserService.updateAcUserImageById(acUserImage, content);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  获取用户推送消息
     * @param acUserId
     * @return
     */
    @PostMapping("/findNotifyByUserId")
    @ApiOperation(value ="获取用户推送消息",notes="传入：编号（acUserId)必传")
    @ApiImplicitParam(name="acUserId",value = "编号",required = true, dataType = "String",paramType = "query")
    public ResponseEntity<Result> findNotifyByUserId(String acUserId) {
        if (StringUtils.isEmpty(acUserId)) {
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        Notify notify = notifyService.selectOne(new Notify(acUserId));
        if (null != notify) {
            return ResultUtil.successResponse(notify);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  获取用户隐私设置
     * @param acUserId
     * @return
     */
    @PostMapping("/findPrivacyByUserId")
    @ApiOperation(value ="获取用户隐私设置",notes="传入：编号（acUserId)必传")
    @ApiImplicitParam(name="acUserId",value = "编号",required = true, dataType = "String",paramType = "query")
    public ResponseEntity<Result> findPrivacyByUserId(String acUserId) {
        if (StringUtils.isEmpty(acUserId)) {
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        Privacy privacy = privacyService.selectOne(new Privacy(acUserId));
        if (null != privacy) {
            return ResultUtil.successResponse(privacy);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据字典获取系统用户id和昵称
     * @return
     */
    @PostMapping("/findUserIdAndUserNameByNo")
    @ApiOperation(value ="根据字典获取系统用户id和昵称")
    public ResponseEntity<Result> findUserIdAndUserNameByNo() {
        return ResultUtil.successResponse(acUserService.findUserIdAndUserNameByNo());
    }

}
