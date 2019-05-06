package com.ynw.system.service;

import com.ynw.system.dao.LoveMapper;
import com.ynw.system.entity.Love;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@Service
@Transactional
public class LoveService {

    @Autowired
    private LoveMapper loveMapper;

    /**
     *  条件查询情豆
     * @param love
     * @param beginDate 查询的开始时间
     * @param endDate 查询的结束时间
     * @param start
     * @param pageSize
     * @return
     */
    public List<Love> conditionQueryLove(Love love, String beginDate, String endDate, Integer start, Integer pageSize) {
        return loveMapper.conditionQueryLove(love, beginDate, endDate, start, pageSize);
    }

    /**
     *  条件查询数据总和
     * @param love
     * @param beginDate
     * @param endDate
     * @return
     */
    public Integer count(Love love, String beginDate, String endDate) {
        return loveMapper.count(love, beginDate, endDate);
    }

    /**
     *  查询当天情豆发放数
     * @param nowTime
     * @return
     */
    public Integer nowCountGrant(String nowTime) {
        return loveMapper.nowCountGrant(nowTime);
    }

    /**
     *  查询当天情豆回收数
     * @param nowTime
     * @return
     */
    public Integer nowCountRecycle(String nowTime) {
        return loveMapper.nowCountRecycle(nowTime);
    }

    /**
     *  查询当天情豆转移数
     * @param nowTime
     * @return
     */
    public Integer nowCountTransfer(String nowTime) {
        return loveMapper.nowCountTransfer(nowTime);
    }

}
