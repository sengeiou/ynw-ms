package com.ynw.system.dao;

import com.ynw.system.entity.Channel;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ChannelMapper extends MyMapper<Channel> {

    List<Channel> conditionQueryChannel(@Param("channel") Channel channel, @Param("pageSize") Integer pageSize, @Param("start") Integer start);

    Integer count(Channel channel);

}
