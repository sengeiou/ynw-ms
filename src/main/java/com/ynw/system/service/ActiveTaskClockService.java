package com.ynw.system.service;

import com.ynw.system.dao.ActiveTaskClockMapper;
import com.ynw.system.entity.ActiveTaskClock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ActiveTaskClockService extends MyService<ActiveTaskClockMapper, ActiveTaskClock> {

    /**
     *  根据活动编号查询活动任务并排序
     * @param registerId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ActiveTaskClock> findAllByActivityId(String registerId) {
        return dao.findAllByActivityId(registerId);
    }

}
