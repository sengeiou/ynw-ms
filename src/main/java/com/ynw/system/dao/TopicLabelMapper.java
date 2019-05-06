package com.ynw.system.dao;

import com.ynw.system.entity.TopicLabel;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface TopicLabelMapper extends MyMapper<TopicLabel> {

    List<TopicLabel> findAll();

    List<TopicLabel> findBySortMax();

    /**
     *  更新
     * @param topicLabel
     * @return
     */
    Integer update(TopicLabel topicLabel);

}
