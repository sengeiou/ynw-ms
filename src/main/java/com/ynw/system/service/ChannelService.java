package com.ynw.system.service;

import com.ynw.system.dao.ChannelMapper;
import com.ynw.system.entity.Channel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChannelService extends MyService<ChannelMapper, Channel> {

    /**
     *  根据条件分页查询数据
     * @param channel
     * @param pageSize
     * @param start
     * @return
     */
    @Transactional(readOnly = true)
    public List<Channel> conditionQueryChannel(Channel channel, Integer pageSize, Integer start) {
        return dao.conditionQueryChannel(channel, pageSize, start);
    }

    /**
     *  条件查询数据总数
     * @param channel
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Channel channel) {
        return dao.count(channel);
    }

}
