package com.ynw.system.controller;

import com.ynw.system.entity.AcUser;
import com.ynw.system.entity.Attention;
import com.ynw.system.entity.Friend;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.AttentionService;
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
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@RestController
@RequestMapping("/ynw-ms/attention")
@Api(tags = "用户好友关注接口",description = "用户好友关注接口")
public class AttentionController {

    @Autowired
    private AttentionService attentionService;

    /**
     *  条件获取关注数据
     * @param acUser
     * @param provinceId 省份编号
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryAttention")
    @ApiOperation(value = "条件获取关注数据", notes = "传入：页码（nowPage必传），手机号（phoneNumber），性别（sex）,省份编号(provinceId)," +
            ",用户当前所在城市ID(bdCityId)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "provinceId", value = "省份编号", dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> conditionQueryAttention(AcUser acUser, String provinceId, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            if (StringUtils.isEmpty(provinceId)) {
                provinceId = null;
            }
            if (StringUtils.isEmpty(acUser.getBdCityId())) {
                acUser.setBdCityId(null);
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Attention> attentionList = attentionService.conditionQueryAttention(acUser, provinceId, start, pageSize);
            if (attentionList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                if (StringUtils.isNotEmpty(url)) {
                    for (Attention attention: attentionList
                    ) {
                        //出现url拼接重复现象，判断路径完整的情况下不再拼接url
                        if (attention.getObjUser().getHeadImageUrl().indexOf(url) == -1) {
                            attention.getObjUser().setHeadImageUrl(url + attention.getObjUser().getHeadImageUrl());
                        }
                        if (attention.getSrcUser().getHeadImageUrl().indexOf(url) == -1) {
                            attention.getSrcUser().setHeadImageUrl(url + attention.getSrcUser().getHeadImageUrl());
                        }
                    }
                }
                return ResultUtil.successResponse(attentionList);
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
        return ResultUtil.successResponse(attentionService.count(acUser, provinceId));
    }

}
