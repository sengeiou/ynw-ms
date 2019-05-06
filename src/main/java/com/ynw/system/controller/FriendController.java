package com.ynw.system.controller;

import com.ynw.system.entity.AcUser;
import com.ynw.system.entity.Friend;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.AcUserService;
import com.ynw.system.service.FriendService;
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
 * 好友关系查询
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@RestController
@RequestMapping("/ynw-ms/friend")
@Api(tags = "用户好友接口",description = "用户好友查询接口")
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     *  条件获取用户数据
     * @param acUser
     * @param provinceId 省份编号
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryFriend")
    @ApiOperation(value = "条件获取用户数据", notes = "传入：页码（nowPage必传），手机号（phoneNumber），性别（sex）,省份编号(provinceId),\" +\n" +
            "            \",用户当前所在城市ID(bdCityId)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "provinceId", value = "省份编号", dataType = "String", paramType = "query"),
    })

    public ResponseEntity<Result> conditionQueryFriend(AcUser acUser, String provinceId, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            if (StringUtils.isEmpty(provinceId)) {
                provinceId = null;
            }
            if (StringUtils.isEmpty(acUser.getBdCityId())) {
                acUser.setBdCityId(null);
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Friend> friendList = friendService.conditionQueryFriend(acUser, provinceId, start, pageSize);
            if (friendList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                if (StringUtils.isNotEmpty(url)) {
                    for (Friend friend: friendList
                    ) {
                        //出现url拼接重复现象，判断路径完整的情况下不再拼接url
                        if (friend.getReqUser().getHeadImageUrl().indexOf(url) == -1) {
                            friend.getReqUser().setHeadImageUrl(url+friend.getReqUser().getHeadImageUrl());
                        }
                        if (friend.getResUser().getHeadImageUrl().indexOf(url) == -1) {
                            friend.getResUser().setHeadImageUrl(url+friend.getResUser().getHeadImageUrl());
                        }
                    }
                }
                return ResultUtil.successResponse(friendList);
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
    @ApiOperation(value = "条件获取数据总数", notes = "传入：手机号（phoneNumber），性别（sex）,省份编号(provinceId)," +
            ",用户当前所在城市ID(bdCityId)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "provinceId", value = "省份编号", dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> count(AcUser acUser, String provinceId) {
        if (StringUtils.isEmpty(provinceId)) {
            provinceId = null;
        }
        if (StringUtils.isEmpty(acUser.getBdCityId())) {
            acUser.setBdCityId(null);
        }
        return ResultUtil.successResponse(friendService.count(acUser, provinceId));
    }

}
