package com.ynw.system.dao;

import com.ynw.system.entity.Subject;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface SubjectMapper extends MyMapper<Subject> {

    List<Subject> conditionQuerySubject(Subject subject);

    Subject findBySortMax(Subject subject);

    List<Subject> findByExamIdAndSort(Subject subject);

}
