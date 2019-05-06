package com.ynw.system.controller;

import com.ynw.system.entity.OptLog;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.OptLogService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
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

import java.util.Date;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@RestController
@RequestMapping("/ynw-ms/optLog")
@Api(tags = "日志接口",description = "日志查询接口")
public class OptLogController {

    @Autowired
    private OptLogService optLogService;

    /**
     * 条件查询日志
     * @param optLog
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryOptLog")
    @ApiOperation(value = "条件查询日志", notes = "传入：页码（nowPage必传），操作员名字（msUserName），" +
            "开始时间（beginDate）,结束时间（endDate）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1") ,
            @ApiImplicitParam(name = "beginDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间",dataType = "String", paramType = "query") ,
    })
    public ResponseEntity<Result> conditionQueryOptLog(OptLog optLog, String beginDate, String endDate, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            if (StringUtils.isEmpty(beginDate)) {
                beginDate = null;
            }
            if (StringUtils.isEmpty(endDate)) {
                endDate = null;
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<OptLog> optLogList = optLogService.conditionQueryOptLog(optLog, beginDate, endDate, start, pageSize);
            if (optLogList.size() > 0) {
                return ResultUtil.successResponse(optLogList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总数
     * @param optLog
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总数", notes = "传入：操作员名字（msUserName），" +
            "开始时间（beginDate）,结束时间（endDate）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginDate", value = "开始时间", dataType = "String", paramType = "query") ,
            @ApiImplicitParam(name = "endDate", value = "结束时间",dataType = "String", paramType = "query") ,
    })
    public ResponseEntity<Result> count(OptLog optLog, String beginDate, String endDate) {
        if (StringUtils.isEmpty(beginDate)) {
            beginDate = null;
        }
        if (StringUtils.isEmpty(endDate)) {
            endDate = null;
        }
        return ResultUtil.successResponse(optLogService.count(optLog, beginDate, endDate));
    }

}
