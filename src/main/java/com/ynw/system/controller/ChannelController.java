package com.ynw.system.controller;

import com.ynw.system.entity.Channel;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ChannelService;
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
@RequestMapping("/ynw-ms/channel")
@Api(tags = "雷达搜频道接口",description = "雷达搜频道查询及操作接口")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    /**
     *  根据条件分页查询数据
     * @param channel
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryChannel")
    @ApiOperation(value = "根据条件分页查询数据", notes = "传入：页码（nowPage必传），频道名（name）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryChannel(Channel channel, Integer nowPage) {
        if (null == nowPage || nowPage == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<Channel> channelList = channelService.conditionQueryChannel(channel, pageSize, start);
        if (channelList.size() > 0)
            return ResultUtil.successResponse(channelList);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *   条件查询数据总数
     * @param channel
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总数", notes = "传入：频道名（name）")
    public ResponseEntity<Result> count(Channel channel) {
        return ResultUtil.successResponse(channelService.count(channel));
    }

    /**
     *  添加雷达频道
     * @param channel
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加雷达频道", notes = "传入：频道名（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Channel channel) {
        if (StringUtils.isEmpty(channel.getName()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (channelService.insert(channel) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  修改雷达频道
     * @param channel
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改雷达频道", notes = "传入：雷达搜频道Id（rsChannelId)，频道名（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Channel channel) {
        if (StringUtils.isEmpty(channel.getName()) || StringUtils.isEmpty(channel.getRsChannelId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Channel channel1 = channelService.selectOne(new Channel(channel.getRsChannelId()));
        if (null == channel1)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        channel1.setName(channel.getName());
        if (channelService.update(channel1) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  删除雷达频道
     * @param channel
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除雷达频道", notes = "传入：雷达搜频道Id（rsChannelId)，添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Channel channel) {
        if (StringUtils.isEmpty(channel.getRsChannelId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (channelService.delete(channel) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

}
