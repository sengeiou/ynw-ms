package com.ynw.system.service;

import com.ynw.system.dao.StatisticsMapper;
import com.ynw.system.entity.Statistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;


    public Statistics statistics(String dateString) {
        Statistics statistics = new Statistics();
        statistics.setAcUserCount(statisticsMapper.acUserCount());
        statistics.setNowAcUserCount(statisticsMapper.nowCount(dateString));
        statistics.setNowAttention(statisticsMapper.nowAttention(dateString));
        statistics.setNowFriend(statisticsMapper.nowFriend(dateString));
        statistics.setNowSignIn(statisticsMapper.nowSignIn(dateString));
        statistics.setNowMood(statisticsMapper.nowMood(dateString));
        statistics.setNowLoveGrant(statisticsMapper.nowLoveGrant(dateString));
        statistics.setNowLoveRecycle(statisticsMapper.nowLoveRecycle(dateString));
        statistics.setNowLoveTransfer(statisticsMapper.nowLoveTransfer(dateString));
        statistics.setNowExamText(statisticsMapper.nowExamText(dateString));
        statistics.setNowExamUserNum(statisticsMapper.nowExamUserNum(dateString));
        return statistics;
    }

}
