package com.ynw.system.service;

import com.ynw.system.dao.OptLogMapper;
import com.ynw.system.entity.OptLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@Service
@Transactional
public class OptLogService extends MyService<OptLogMapper, OptLog> {

    /**
     * 条件查询日志
     * @param optLog
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @param start
     * @param pageSize
     * @return
     */
    public List<OptLog> conditionQueryOptLog(OptLog optLog, String beginDate, String endDate, Integer start, Integer pageSize) {
        return dao.conditionQueryOptLog(optLog, beginDate, endDate, start, pageSize);
    }

    /**
     *  条件查询数据总数
     * @param optLog
     * @param beginDate 开始时间
     * @param endDate 结束时间
     * @return
     */
    public Integer count(OptLog optLog, String beginDate, String endDate) {
        return dao.count(optLog, beginDate, endDate);
    }

}
