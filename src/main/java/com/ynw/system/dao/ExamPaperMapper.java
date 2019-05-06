package com.ynw.system.dao;

import com.ynw.system.entity.ExamPaper;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExamPaperMapper extends MyMapper<ExamPaper> {

    List<ExamPaper> conditionQueryExamPaper(@Param("examPaper") ExamPaper examPaper, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer count(ExamPaper examPaper);

    ExamPaper findBySortMax();

}
