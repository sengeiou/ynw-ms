package com.ynw.system.controller;

import com.ynw.system.entity.*;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.AcUserService;
import com.ynw.system.service.ActiveTaskClockService;
import com.ynw.system.service.PushService;
import com.ynw.system.service.RegisterService;
import com.ynw.system.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ynw-ms/push")
@Api(tags = "消息推送接口",description = "消息推送和用户反馈查询及操作接口")
public class PushController {

    @Autowired
    private PushService pushService;
    @Autowired
    private AcUserService acUserService;
    @Autowired
    private HandleFile handleFile;

    /**
     *  根据条件分页查询数据
     * @param body
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryPshBody")
    @ApiOperation(value = "根据条件分页查询消息推送记录", notes = "传入：页码（nowPage必传），消息标题（title），消息业务类型（businessType），" +
            "消息类型（type），消息分组（group），消息目标范围（sendScope），消息是否已经发送（send）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryPshBody(PushBody body, Integer nowPage) {
        if (null == nowPage || nowPage == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<PushBody> pushBodyList = pushService.conditionQueryPshBody(body, pageSize, start);
        if (pushBodyList.size() > 0) {
            String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
            for (PushBody pushBody: pushBodyList
                 ) {
                if (null != pushBody.getImageUrl())
                    pushBody.setImageUrl(url + pushBody.getImageUrl());
            }
            return ResultUtil.successResponse(pushBodyList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据条件分页查询数据
     * @param advice
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryAdvice")
    @ApiOperation(value = "根据条件分页查询用户反馈记录", notes = "传入：页码（nowPage必传），用户姓名（userName），反馈内容类型（type），" +
            "是否已回复（reply）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "reply", value = "是否已回复", dataType = "Long", paramType = "query",example = "1"),
    })
    public ResponseEntity<Result> conditionQueryAdvice(Advice advice, Integer nowPage, Integer reply) {
        if (null == nowPage || nowPage == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<Advice> adviceList = pushService.conditionQueryAdvice(advice, pageSize, start, reply);
        if (adviceList.size() > 0) {
            String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
            for (Advice advices: adviceList
            ) {
                if (advices.getType() == 1)
                    advices.setContent(url + advices.getContent());
            }
            return ResultUtil.successResponse(adviceList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据条件查询数据总数
     * @param body
     * @return
     */
    @PostMapping("/pushBodyCount")
    @ApiOperation(value = "根据条件查询消息推送记录总数", notes = "传入：消息标题（title），消息业务类型（businessType），" +
            "消息类型（type），消息分组（group），消息目标范围（sendScope），消息是否已经发送（send）")
    public ResponseEntity<Result> count(PushBody body) {
        return ResultUtil.successResponse(pushService.count(body));
    }

    /**
     *  根据条件查询数据总数
     * @param advice
     * @return
     */
    @PostMapping("/adviceCount")
    @ApiOperation(value = "根据条件查询用户反馈总数", notes = "传入：用户姓名（userName），反馈内容类型（type），" +
            "是否已回复（reply）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reply", value = "是否已回复", dataType = "Long", paramType = "query",example = "1"),
    })
    public ResponseEntity<Result> count(Advice advice, Integer reply) {
        return ResultUtil.successResponse(pushService.count(advice, reply));
    }

    /**
     * 推送
     * @param body 消息主体
     * @param infoOne 消息业务信息1
     * @param infoTwo 消息业务信息2
     * @param userId 推送对象（单个传入用户编号，全部传入all）
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "消息推送", notes = "传入：消息类型（type）(必传)，消息分组（group）(必传)，" +
            "消息目标范围（sendScope）(必传)，消息范围分支的群发消息：excel文件名（excelName）和excel文件(第一行为用户编号)（file），" +
            "消息范围分支的单点消息：用户编号或者直接赋值all（userId），业务类型（businessType）(必传)，消息内容（content）(必传)，" +
            "消息开始时间（beginTime），消息结束时间（endTime），排序（sort），图片文件（fileName）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "infoOne", value = "消息业务信息1", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "infoTwo", value = "消息业务信息2", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "推送对象（单个传入用户编号，全部传入all）", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "businessType", value = "业务类型",required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "excelName", value = "excel的文件名", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "excel文件", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "fileName", value = "图片文件", dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> insertPush(PushBody body, String infoOne, String infoTwo, String userId,
                                             String businessType, String excelName,HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(body.getType()) || StringUtils.isEmpty(businessType)
                || StringUtils.isEmpty(body.getGroup()) || StringUtils.isEmpty(body.getTitle()) || StringUtils.isEmpty(body.getSendScope()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (StringUtils.isNotEmpty(body.getImageUrl())) {
            String pictureUrl = upload(request, "fileName");
            if (null == pictureUrl) {
               return ResultUtil.errorResponse(ResultEnums.FILE_ERROR);
            }
            body.setImageUrl(pictureUrl);
        } else {
            body.setImageUrl(null);
        }
        if (StringUtils.isEmpty(excelName)) {
            //文件路径为空，单个推送或者全部推送
            if (!userId.equals("all") && StringUtils.isNotEmpty(userId)) {
                AcUser user = new AcUser();
                //查询编号
                user.setNo(userId);
                AcUser acUser = acUserService.selectOne(user);
                if (null == acUser) {
                    //查询id
                    user.setNo(null);
                    user.setAcUserId(userId);
                    acUser = acUserService.selectOne(user);
                    if (null == acUser)
                        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
                }
                userId = acUser.getAcUserId();
            }
            pushService.insertMgBody(infoOne, infoTwo, true, body, businessType, userId);
        } else {
            //根据文件路径获取excel转成数据数据
            excelName = upload(request, "file");
            if (StringUtils.isEmpty(excelName))
                return ResultUtil.errorResponse(ResultEnums.UPLOADING_EXCEL);
            excelName = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH") + excelName;
//            System.out.println(excelName);
            List<String> list = DataToExcel.getAllByExcel(excelName);
            String[] strings = list.toArray(new String[0]);
            pushService.insertMgBody(infoOne, infoTwo, true, body, businessType, strings);
        }
        return ResultUtil.successResponse();
    }

    /**
     *  上传图片
     * @param request
     * @return
     */
    public String upload(HttpServletRequest request, String fileName) throws IOException {
        List<MultipartFile> file1 = new ArrayList<MultipartFile>();
        if (request instanceof MultipartHttpServletRequest) {
            file1 = ((MultipartHttpServletRequest) request).getFiles(fileName);
        }
        if (file1.size() > 0) {
            if (file1.get(0).getSize() == 0)
                return null;
            return handleFile.saveFile(file1.get(0) ,"push");
        }
        return null;
    }

}
