package com.ynw.system.service;

import com.ynw.system.dao.RadarSubjectMapper;
import com.ynw.system.entity.RadarSubject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RadarSubjectService extends MyService<RadarSubjectMapper, RadarSubject> {

    /**
     *  根据条件分页查询数据
     * @param subject
     * @param pageSize
     * @param start
     * @return
     */
    @Transactional(readOnly = true)
    public List<RadarSubject> conditionQueryRadarSubject(RadarSubject subject, Integer pageSize, Integer start) {
        return dao.conditionQueryRadarSubject(subject, pageSize, start);
    }

    /**
     *  条件统计数据总数
     * @param subject
     * @return
     */
    @Transactional(readOnly = true)
    public Integer cpunt(RadarSubject subject) {
        return dao.count(subject);
    }

    /**
     *  根据条件查询排序后的数据
     * @param subject
     * @return
     */
    public List<RadarSubject> findBySortMax(RadarSubject subject) {
        return dao.findBySortMax(subject);
    }

}
