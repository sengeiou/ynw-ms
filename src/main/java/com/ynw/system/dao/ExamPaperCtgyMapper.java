package com.ynw.system.dao;

import com.ynw.system.entity.ExamPaperCtgy;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface ExamPaperCtgyMapper extends MyMapper<ExamPaperCtgy> {

    public List<ExamPaperCtgy> findAll();

    List<ExamPaperCtgy> findBySortMax();

}
