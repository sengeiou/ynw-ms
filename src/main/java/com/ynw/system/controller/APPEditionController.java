package com.ynw.system.controller;

import com.ynw.system.entity.APPEdition;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.APPEditionService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@RestController
@RequestMapping("/ynw-ms/appEdition")
@Api(tags = "app版本接口",description = "app版本查询及操作接口")
public class APPEditionController {

    @Autowired
    private APPEditionService appEditionService;

    /**
     *  条件查询app版本信息
     * @param appEdition
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryAPPEdition")
    @ApiOperation(value = "根据条件分页查询用户标签记录", notes = "传入：页码（nowPage必传），版本号（number），系统类型（visitorOsType）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryAPPEdition(APPEdition appEdition, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<APPEdition> appEditionList = appEditionService.conditionQueryResourceAll(appEdition, start, pageSize);
            if (appEditionList.size() > 0) {
                return ResultUtil.successResponse(appEditionList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据数量
     * @param appEdition
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据数量", notes = "传入：版本号（number），系统类型（visitorOsType）")
    public ResponseEntity<Result> count(APPEdition appEdition) {
        return ResultUtil.successResponse(appEditionService.count(appEdition));
    }

    /**
     *  添加app版本信息
     * @param appEdition
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加app版本信息", notes = "传入：版本号（number），版本内容（content），安装包url（packageUrl）" +
            "，是否强制更新（isForceUpdate），版本更新描述（describe），系统类型（visitorOsType），版本大小（size）" +
            "，添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(APPEdition appEdition) {
        if (StringUtils.isNotEmpty(appEdition.getContent()) && StringUtils.isNotEmpty(appEdition.getDescribe())
                && StringUtils.isNotEmpty(appEdition.getNumber()) && StringUtils.isNotEmpty(appEdition.getPackageUrl())
                && StringUtils.isNotEmpty(appEdition.getVisitorOsType()) && StringUtils.isNotEmpty(appEdition.getSize())) {
            if (appEditionService.insert(appEdition) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改app版本信息
     * @param appEdition
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改app版本信息", notes = "传入：版本的id（msAppVUpdateId），版本号（number），版本内容（content），安装包url（packageUrl）" +
            "，是否强制更新（isForceUpdate），版本更新描述（describe），系统类型（visitorOsType），版本大小（size）" +
            "，添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(APPEdition appEdition) {
        if (StringUtils.isNotEmpty(appEdition.getContent()) && StringUtils.isNotEmpty(appEdition.getDescribe())
                && StringUtils.isNotEmpty(appEdition.getNumber()) && StringUtils.isNotEmpty(appEdition.getPackageUrl())
                && StringUtils.isNotEmpty(appEdition.getVisitorOsType()) && StringUtils.isNotEmpty(appEdition.getSize())
                && StringUtils.isNotEmpty(appEdition.getMsAppVUpdateId())) {
            APPEdition appEdition1 = appEditionService.selectOne(new APPEdition(appEdition.getMsAppVUpdateId()));
            if(null != appEdition1) {
                //赋值
                appEdition1.setContent(appEdition.getContent());
                appEdition1.setDescribe(appEdition.getDescribe());
                appEdition1.setIsForceUpdate(appEdition.getIsForceUpdate());
                appEdition1.setNumber(appEdition.getNumber());
                appEdition1.setPackageUrl(appEdition.getPackageUrl());
                appEdition1.setSize(appEdition.getSize());
                appEdition1.setVisitorOsType(appEdition.getVisitorOsType());
                if (appEditionService.update(appEdition1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除app版本信息
     * @param appEdition
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除app版本信息", notes = "传入：版本的id（msAppVUpdateId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(APPEdition appEdition) {
        if (StringUtils.isNotEmpty(appEdition.getMsAppVUpdateId())) {
            if (appEditionService.delete(appEdition) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  获取单个app版本信息
     * @param appEdition
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "获取单个app版本信息", notes = "传入：版本的id（msAppVUpdateId），版本号（number），版本内容（content），安装包url（packageUrl）" +
            "，是否强制更新（isForceUpdate），版本更新描述（describe），系统类型（visitorOsType），版本大小（size）" +
            "")
    public ResponseEntity<Result> findById(APPEdition appEdition) {
        if (StringUtils.isNotEmpty(appEdition.getMsAppVUpdateId())) {
            APPEdition appEdition1 = appEditionService.selectOne(appEdition);
            if (null != appEdition1) {
                return ResultUtil.successResponse(appEdition1);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
