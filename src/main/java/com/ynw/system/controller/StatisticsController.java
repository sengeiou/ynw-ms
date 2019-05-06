package com.ynw.system.controller;

import com.ynw.system.entity.AcUser;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.*;
import com.ynw.system.util.DateUtils;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Api(tags = "首页统计接口",description = "首页统计接口")
public class StatisticsController {

    @Autowired
    private AcUserService acUserService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private SignInService signInService;
    @Autowired
    private MoodService moodService;
    @Autowired
    private LoveService loveService;
    @Autowired
    private ExamPaperTestService examPaperTestService;

    @PostMapping("/ynw-ms/statistics")
    @ApiOperation(value = "统计信息")
    public ResponseEntity<Result> statistics() {
        String date = DateUtils.dateStringShort();
        Map<String,Integer> map = new HashMap<>();//存放统计数值
        Integer acUserCount = acUserService.count(new AcUser(),null);
        map.put("acUser",acUserCount);
        Integer nowAcUserCount = acUserService.nowCount(date);
        map.put("nowAcUser", nowAcUserCount);
        Integer nowAttention = attentionService.nowCount(date);
        map.put("nowAttention",nowAttention);
        Integer nowFriend = friendService.nowCount(date);
        map.put("nowFriend",nowFriend);
        Integer nowSignIn = signInService.nowCount(date);
        map.put("nowSignIn",nowSignIn);
        Integer nowMood = moodService.nowCount(date);
        map.put("nowMood",nowMood);
        //情豆部分因为提前扣除问题暂时显示，后期再做修改
//        Integer nowLoveGrant = loveService.nowCountGrant(date);
        Integer nowLoveGrant = (null == loveService.nowCountGrant(date))?0:loveService.nowCountGrant(date);
                map.put("nowLoveGrant",nowLoveGrant);
//        Integer nowLoveRecycle = loveService.nowCountRecycle(date);
        Integer nowLoveRecycle = (null == loveService.nowCountRecycle(date))?0:loveService.nowCountRecycle(date);
        map.put("nowLoveRecycle",nowLoveRecycle);
        Integer nowLoveTransfer = (null == loveService.nowCountTransfer(date))?0:loveService.nowCountTransfer(date);
//        Integer nowLoveTransfer = loveService.nowCountTransfer(date);
        map.put("nowLoveTransfer",nowLoveTransfer);
        Integer nowExamText = examPaperTestService.nowCount(date);
        map.put("nowExamText",nowExamText);
        Integer nowExamUserNum = examPaperTestService.nowCountByUserId(date);
        map.put("nowExamUserNum",nowExamUserNum);
        return ResultUtil.successResponse(map);
    }

}
