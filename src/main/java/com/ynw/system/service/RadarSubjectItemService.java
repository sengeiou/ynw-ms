package com.ynw.system.service;

import com.ynw.system.dao.RadarSubjectItemMapper;
import com.ynw.system.entity.RadarSubjectItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RadarSubjectItemService extends MyService<RadarSubjectItemMapper, RadarSubjectItem> {

    /**
     *  根据题目编号正序查询所有选项
     * @param subjectId
     * @return
     */
    public List<RadarSubjectItem> findAllBySubjectId(String subjectId) {
        return dao.findAllBySubjectId(subjectId);
    }

    /**
     *  根据题目编号逆序查询所有选项
     * @param subjectId
     * @return
     */
    public List<RadarSubjectItem> findBySortMax(String subjectId) {
        return dao.findBySortMax(subjectId);
    }

}
