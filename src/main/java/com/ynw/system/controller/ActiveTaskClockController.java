package com.ynw.system.controller;

import com.ynw.system.entity.ActiveTaskClock;
import com.ynw.system.entity.ActiveTaskClockRegister;
import com.ynw.system.entity.ImGroup;
import com.ynw.system.entity.Register;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.*;
import com.ynw.system.util.DateUtils;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(tags = "消息活动任务接口",description = "消息活动任务记录及推送接口")
@RestController
@RequestMapping("/ynw-ms/activeTaskClock")
public class ActiveTaskClockController {

    @Autowired
    private ActiveTaskClockRegisterService activeTaskClockRegisterService;
    @Autowired
    private ActiveTaskClockService clockService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private ImGroupService imGroupService;
    @Autowired
    private HuanXinService huanXinService;

    /**
     *  活动任务群消息推送
     * @param atRegisterId 活动id
     * @return
     */
    @ApiOperation(value ="活动任务群消息推送",notes="传入：活动编号（atRegisterId）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="atRegisterId",value = "活动编号",required = true,dataType = "String",paramType = "query")
    })
    @PostMapping("/taskToPush")
    public ResponseEntity<Result> taskToPush(String atRegisterId) {
        if (StringUtils.isEmpty(atRegisterId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Register register = registerService.selectOne(new Register(atRegisterId));
        if (null != register) {
            if (DateUtils.dateDiff(register.getBeginTime(), new Date()) < 0)
                return ResultUtil.errorResponse(ResultEnums.ACTIVITY_NOT_BEGIN);
            if (DateUtils.dateDiff(register.getEndTime(), new Date()) > 0)
                return ResultUtil.errorResponse(ResultEnums.ACTIVITY_NOT_OVER);
            Integer days = DateUtils.getDiffDays(register.getBeginTime(), new Date());
            ActiveTaskClock activeTaskClock = clockService.selectOne(new ActiveTaskClock(atRegisterId, days+1));
            if (null != activeTaskClock) {
                List<ImGroup> groupList = imGroupService.selectList(new ImGroup("IGBT_WEEKCP",atRegisterId,1));
                if (groupList.size() > 0) {
                    List<String> target = new ArrayList<>();
                    for (ImGroup imGroup: groupList
                    ) {
                        target.add(imGroup.getHxGroupId());
                    }
                    huanXinService.sendMessage("chatgroups", target, "txt", "第"+ (days+1) +"天任务："+activeTaskClock.getTaskDes(), "一周情侣工作人员");
                    return ResultUtil.successResponse();
                }
            }
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据条件分页查询数据
     * @param register
     * @param nowPage
     * @return
     */
    @ApiOperation(value = "根据条件分页查询推送记录", notes = "传入：活动编号（atRegisterId必传），页码（nowPage必传），情侣名字（resName），编号（matchNo），打卡第几天（missionDay）" +
            "，状态（status）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    })
    @PostMapping("/conditionQueryActiveTaskClockRegister")
    public ResponseEntity<Result> conditionQueryActiveTaskClockRegister(ActiveTaskClockRegister register, Integer nowPage) {
        if (null == nowPage || nowPage == 0 || StringUtils.isEmpty(register.getAtRegisterId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<ActiveTaskClockRegister> registerList = activeTaskClockRegisterService.conditionQueryActiveTaskClockRegister(register, pageSize, start);
        if (registerList.size() > 0)
            return ResultUtil.successResponse(registerList);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据条件查找数据总数
     * @param register
     * @return
     */
    @ApiOperation(value = "根据条件查找数据总数", notes = "传入：活动编号（atRegisterId必传），页码（nowPage必传），情侣名字（resName），编号（matchNo），打卡第几天（missionDay）" +
            "，状态（status）")
    @PostMapping("/count")
    public ResponseEntity<Result> count(ActiveTaskClockRegister register) {
        return ResultUtil.successResponse(activeTaskClockRegisterService.count(register));
    }

    /**
     *  根据活动编号查询活动任务并排序
     * @param registerId
     * @return
     */
    @ApiOperation(value ="根据活动编号查询活动任务并排序",notes="传入：活动编号（atRegisterId）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="atRegisterId",value = "活动编号",required = true,dataType = "String",paramType = "query")
    })
    @PostMapping("/findAllByActivityId")
    public ResponseEntity<Result> findAllByActivityId(String registerId) {
        if (StringUtils.isEmpty(registerId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        List<ActiveTaskClock> clockList = clockService.findAllByActivityId(registerId);
        if (clockList.size() > 0)
            return ResultUtil.successResponse(clockList);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据编号查询任务活动
     * @param atWkcpTaskId 任务编号
     * @return
     */
    @ApiOperation(value ="根据编号查询任务活动",notes="传入：任务编号（atWkcpTaskId）")
    @ApiImplicitParams({
            @ApiImplicitParam(name="atWkcpTaskId",value = "任务编号",required = true,dataType = "String",paramType = "query")
    })
    @PostMapping("/findById")
    public ResponseEntity<Result> findById(String atWkcpTaskId) {
        if (StringUtils.isEmpty(atWkcpTaskId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        ActiveTaskClock clock = clockService.selectOne(new ActiveTaskClock(atWkcpTaskId));
        if (null != clock)
            return ResultUtil.successResponse(clock);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加活动任务
     * @param clock
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value ="添加活动任务",notes="传入：活动登记id（atRegisterId）,活动第几天（day）,任务描述（taskDes）," +
            "日志主体（LogContent）")
    public ResponseEntity<Result> insert(ActiveTaskClock clock) {
        if (StringUtils.isEmpty(clock.getAtRegisterId()) || StringUtils.isEmpty(clock.getTaskDes())
                || null == clock.getDay() || clock.getDay() == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        ActiveTaskClock taskClock = new ActiveTaskClock();
        taskClock.setAtRegisterId(clock.getAtRegisterId());
        taskClock.setDay(clock.getDay());
        if (null != clockService.selectOne(taskClock))
            return ResultUtil.errorResponse(ResultEnums.SAME_DAY_TASK);
        if (clockService.insert(clock) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  更新活动任务
     * @param clock
     * @return
     */
    @ApiOperation(value ="添加活动任务",notes="传入：活动任务id（atWkcpTaskId）,活动登记id（atRegisterId）,活动第几天（day）,任务描述（taskDes）," +
            "日志主体（LogContent）")
    @PostMapping("/update")
    public ResponseEntity<Result> update(ActiveTaskClock clock) {
        if (StringUtils.isEmpty(clock.getAtWkcpTaskId()) || StringUtils.isEmpty(clock.getTaskDes()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        ActiveTaskClock taskClock = clockService.selectOne(new ActiveTaskClock(clock.getAtWkcpTaskId()));
        if (null == taskClock)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        taskClock.setTaskDes(clock.getTaskDes());
        if (clockService.update(taskClock) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  删除活动任务
     * @param clock
     * @return
     */
    @ApiOperation(value ="添加活动任务",notes="传入：活动任务id（atWkcpTaskId）," +
            "日志主体（LogContent）")
    @PostMapping("/delete")
    public ResponseEntity<Result> delete(ActiveTaskClock clock) {
        if (StringUtils.isEmpty(clock.getAtWkcpTaskId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (clockService.delete(clock) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

}
