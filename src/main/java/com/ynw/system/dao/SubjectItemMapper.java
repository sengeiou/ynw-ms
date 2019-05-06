package com.ynw.system.dao;

import com.ynw.system.entity.SubjectItem;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface SubjectItemMapper extends MyMapper<SubjectItem> {

    List<SubjectItem> conditionQuerySubjectItem(SubjectItem subjectItem);

    SubjectItem findBySortMax(String kbSubjectId);

}
