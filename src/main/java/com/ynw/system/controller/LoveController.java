package com.ynw.system.controller;

import com.ynw.system.entity.Love;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.LoveService;
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

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@RestController
@RequestMapping("/ynw-ms/love")
@Api(tags = "用户情豆接口",description = "用户情豆查询接口")
public class LoveController {

    @Autowired
    private LoveService loveService;

    /**
     *  条件查询情豆
     * @param love
     * @param beginDate 查询的开始时间
     * @param endDate 查询的结束时间
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryLove")
    @ApiOperation(value = "条件查询情豆", notes = "传入：页码（nowPage必传），手机号码（acUserPhone），情豆出入账类型（type），" +
            "关联业务key（assoBusinessKey）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "beginDate", value = "起始时间", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true, dataType = "String", paramType = "query"),
    })

    public ResponseEntity<Result> conditionQueryLove(Love love, String beginDate, String endDate, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            if (StringUtils.isEmpty(beginDate)) {
                beginDate = null;
            }
            if (StringUtils.isEmpty(endDate)) {
                endDate = null;
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Love> loveList = loveService.conditionQueryLove(love, beginDate, endDate, start, pageSize);
            if (loveList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                for (Love love1: loveList
                ) {
                    love1.setAcUserImg(url + love1.getAcUserImg());
                }
                return ResultUtil.successResponse(loveList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总和
     * @param love
     * @param beginDate 查询的开始时间
     * @param endDate 查询的结束时间
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总和", notes = "传入：手机号码（acUserPhone），情豆出入账类型（type），" +
            "关联业务key（assoBusinessKey）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginDate", value = "起始时间", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", required = true, dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> count(Love love, String beginDate, String endDate) {
        if (StringUtils.isEmpty(beginDate)) {
            beginDate = null;
        }
        if (StringUtils.isEmpty(endDate)) {
            endDate = null;
        }
        return ResultUtil.successResponse(loveService.count(love, beginDate, endDate));
    }

}
