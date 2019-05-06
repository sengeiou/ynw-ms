package com.ynw.system.controller;

import com.ynw.system.entity.InviteTask;
import com.ynw.system.entity.PushBody;
import com.ynw.system.entity.SweetsFlow;
import com.ynw.system.entity.SweetsSum;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.PushService;
import com.ynw.system.service.SweetsFlowService;
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
 *  糖果
 */
@RestController
@RequestMapping("/ynw-ms/sweets")
@Api(tags = "糖果接口",description = "糖果查询及操作接口")
public class SweetsController {

    @Autowired
    private SweetsFlowService sweetsFlowService;


    /**
     *  推送提现失败理由
     * @param userId
     * @param content 推送提现失败理由
     * @param quantity 提现糖果数
     * @param acSugarFlowId 提现流水id
     * @return
     */
    @PostMapping("/withdrawalOfFailure")
    @ApiOperation(value = "推送提现失败理由", notes = "传入：提现用户编号（userId），content（name），提现糖果数（quantity）,提现流水id(acSugarFlowId)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "提现用户编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "content", value = "推送提现失败理由", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "quantity", value = "提现糖果数", required = true, dataType = "Long", paramType = "query",example = "100"),
            @ApiImplicitParam(name = "acSugarFlowId", value = "提现流水id", required = true, dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> withdrawalOfFailure(String userId, String content, Integer quantity, String acSugarFlowId) {
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(content) || StringUtils.isEmpty(acSugarFlowId) || null == quantity || quantity == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        sweetsFlowService.withdrawDeposit(userId, content, quantity, acSugarFlowId);
        return ResultUtil.successResponse();
    }

    /**
     *  条件分页查询糖果任务流水数据
     * @param inviteTask
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryInviteTask")
    @ApiOperation(value = "条件分页查询糖果任务流水数据", notes = "传入：页码（nowPage必传），任务人名字（userName），任务人电话（userPhone）" +
            "，邀请人名字（inviteName），邀请人电话（invitePhone），完成状态（status）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryInviteTask(InviteTask inviteTask, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<InviteTask> inviteTaskList = sweetsFlowService.conditionQueryInviteTask(inviteTask, start, pageSize);
            if (inviteTaskList.size() > 0) {
                return ResultUtil.successResponse(inviteTaskList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  符合条件的总数
     * @param inviteTask
     * @return
     */
    @PostMapping("/countInviteTask")
    @ApiOperation(value = "条件查询糖果任务流水总数", notes = "传入：任务人名字（userName），任务人电话（userPhone）" +
            "，邀请人名字（inviteName），邀请人电话（invitePhone），完成状态（status）")
    public ResponseEntity<Result> countInviteTask(InviteTask inviteTask) {
        return ResultUtil.successResponse(sweetsFlowService.count(inviteTask));
    }

    /**
     *  更新流水和糖豆总数
     * @param sweetsFlow
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新流水和糖豆总数", notes = "传入：糖果流水编号（acSugarFlowId），当次糖果的获得量或者消耗量（quantity），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(SweetsFlow sweetsFlow) {
        if (StringUtils.isEmpty(sweetsFlow.getAcSugarFlowId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (sweetsFlowService.updateType(sweetsFlow) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据id查询数据
     * @param acSugarFlowId
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据id查询糖果流水数据", notes = "传入：糖果流水编号（acSugarFlowId)(必传)")
    @ApiImplicitParam(name = "acSugarFlowId", value = "糖果流水编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findById(String acSugarFlowId) {
        if (StringUtils.isEmpty(acSugarFlowId)) {
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        SweetsFlow sweetsFlow = sweetsFlowService.findById(acSugarFlowId);
        if (null == sweetsFlow)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
        sweetsFlow.setAlipayCode(url + sweetsFlow.getAlipayCode());
        return ResultUtil.successResponse(sweetsFlow);
    }

    /**
     *  条件分页查询数据
     * @param sweetsSum
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQuerySweetsSum")
    @ApiOperation(value = "条件分页查询用户糖果总量数据", notes = "传入：页码（nowPage必传），用户名称（userName），用户手机（userPhone）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQuerySweetsSum(SweetsSum sweetsSum, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<SweetsSum> sweetsSumList = sweetsFlowService.conditionQuerySweetsSum(sweetsSum, start, pageSize);
            if (sweetsSumList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                for (SweetsSum sweetsSums: sweetsSumList
                ) {
                    sweetsSums.setUserImg(url + sweetsSums.getUserImg());
                }
                return ResultUtil.successResponse(sweetsSumList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  符合条件的总数
     * @param sweetsSum
     * @return
     */
    @PostMapping("/countSweetsSum")
    @ApiOperation(value = "条件分页查询用户糖果总量数据", notes = "传入：用户名称（userName），用户手机（userPhone）")
    public ResponseEntity<Result> countSweetsSum(SweetsSum sweetsSum) {
        return ResultUtil.successResponse(sweetsFlowService.count(sweetsSum));
    }

    /**
     *  条件分页查询数据
     * @param sweetsFlow
     * @param beginDate
     * @param endDate
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQuerySweetsFlow")
    @ApiOperation(value = "条件分页查询糖果交易流水", notes = "传入：页码（nowPage必传），用户名（userName），用户手机号（userPhone）" +
            "，糖果的消耗类型（type），取字典表中的key（assoBusinessKey），起始时间（beginDate），结束时间（endDate）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "beginDate", value = "起始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> conditionQuerySweetsFlow(SweetsFlow sweetsFlow, String beginDate, String endDate,
                                                           Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            if (StringUtils.isEmpty(beginDate)) {
                beginDate = null;
            }
            if (StringUtils.isEmpty(endDate)) {
                endDate = null;
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<SweetsFlow> sweetsFlows = sweetsFlowService.conditionQuerySweetsFlow(sweetsFlow,beginDate, endDate, start, pageSize);
            if (sweetsFlows.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                for (SweetsFlow sweetsFlow1: sweetsFlows
                     ) {
                    sweetsFlow1.setUserImg(url + sweetsFlow1.getUserImg());
                }
                return ResultUtil.successResponse(sweetsFlows);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  符合条件的总数
     * @param sweetsFlow
     * @param beginDate
     * @param endDate
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询糖果交易流水总数", notes = "传入：用户名（userName），用户手机号（userPhone）" +
            "，糖果的消耗类型（type），取字典表中的key（assoBusinessKey），起始时间（beginDate），结束时间（endDate）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "beginDate", value = "起始时间", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endDate", value = "结束时间", dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> count(SweetsFlow sweetsFlow, String beginDate, String endDate) {
        if (StringUtils.isEmpty(beginDate)) {
            beginDate = null;
        }
        if (StringUtils.isEmpty(endDate)) {
            endDate = null;
        }
        return ResultUtil.successResponse(sweetsFlowService.count(sweetsFlow, beginDate, endDate));
    }

}
