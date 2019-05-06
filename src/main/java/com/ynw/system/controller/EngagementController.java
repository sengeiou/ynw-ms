package com.ynw.system.controller;

import com.ynw.system.entity.Engagement;
import com.ynw.system.entity.Invite;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.EngagementService;
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

@RestController
@RequestMapping("/ynw-ms/engagement")
@Api(tags = "约活动接口",description = "约活动和报名用户查询接口")
public class EngagementController {

    @Autowired
    private EngagementService engagementService;

    /**
     *  条件分页查询数据
     * @param invite
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryInvite")
    @ApiOperation(value = "条件分页查询约活动报名用户数据", notes = "传入：页码（nowPage必传），活动报名者的真实姓名（realName），联系电话（phoneNumber）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryInvite(Invite invite, Integer nowPage) {
        if (null == nowPage || nowPage < 1 )
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<Invite> inviteList = engagementService.conditionQueryInvite(invite, start, pageSize);
        if (inviteList.size() > 0) {
            return ResultUtil.successResponse(inviteList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  条件查询总数据量
     * @param invite
     * @return
     */
    @PostMapping("/countInvite")
    @ApiOperation(value = "条件查询约活动报名用户总数据量", notes = "传入：活动报名者的真实姓名（realName），联系电话（phoneNumber）")
    public ResponseEntity<Result> count(Invite invite) {
        return ResultUtil.successResponse(engagementService.count(invite));
    }

    /**
     *  更新活动审核状态
     * @param engagement
     * @return
     */
    @PostMapping("/updateStatus")
    @ApiOperation(value = "更新活动审核状态及不通过推送", notes = "传入：活动登记id（atRegisterId），审核状态（checkStatus）,不通过理由（reason）")
    @ApiImplicitParam(name = "reason", value = "不通过理由",dataType = "String", paramType = "query")
    public ResponseEntity<Result> updateStatus(Engagement engagement, String reason) {
        if (StringUtils.isEmpty(engagement.getAtRegisterId()) || null == engagement.getCheckStatus())
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (engagementService.updateCheckStatus(engagement, reason) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  根据编号查询数据
     * @param registerId
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据编号查询数据", notes = "传入：活动登记id（registerId）")
    @ApiImplicitParam(name = "registerId", value = "活动登记id",required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findById(String registerId) {
        if (StringUtils.isEmpty(registerId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Engagement engagement = engagementService.findById(registerId);
        if (null != engagement)
            return ResultUtil.successResponse(engagement);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  条件分页查询数据
     * @param engagement
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryEngagement")
    @ApiOperation(value = "条件分页查询约活动数据", notes = "传入：页码（nowPage必传），活动名称（name），发起人姓名（userName）" +
            "，发起人电话（userPhone），活动分类id（atCtgyId），约活动主题id（atInviteThemeId），" +
            "审核状态（checkStatus），活动开始时间（beginTime）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity conditionQueryEngagement(Engagement engagement, Integer nowPage) {
        if (null == nowPage || nowPage < 1)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<Engagement> engagementList = engagementService.conditionQueryEngagement(engagement, start, pageSize);
        if (engagementList.size() > 0)
            return ResultUtil.successResponse(engagementList);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  条件获取数据总数
     * @param engagement
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件分页查询约活动数据", notes = "传入：活动名称（name），发起人姓名（userName）" +
            "，发起人电话（userPhone），活动分类id（atCtgyId），约活动主题id（atInviteThemeId），" +
            "审核状态（checkStatus），活动开始时间（beginTime）")
    public ResponseEntity<Result> count(Engagement engagement) {
        return ResultUtil.successResponse(engagementService.count(engagement));
    }

}
