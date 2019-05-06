package com.ynw.system.service;

import com.ynw.system.dao.SubjectItemMapper;
import com.ynw.system.entity.SubjectItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/15
 */
@Service
@Transactional
public class SubjectItemService extends MyService<SubjectItemMapper, SubjectItem> {

    /**
     * 根据题目查询题目选项
     * @param subjectItem
     * @return
     */
    public List<SubjectItem> conditionQuerySubjectItem(SubjectItem subjectItem) {
        return dao.conditionQuerySubjectItem(subjectItem);
    }

    /**
     *  根据题目获取降序数据
     *  @param kbSubjectId 题目编号
     * @return
     */
    public SubjectItem findBySortMax(String kbSubjectId) {
        return dao.findBySortMax(kbSubjectId);
    }


}
