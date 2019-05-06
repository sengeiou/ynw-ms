package com.ynw.system.dao;

import com.ynw.system.entity.ExamPaperAnswer;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface ExamPaperAnswerMapper extends MyMapper<ExamPaperAnswer> {

    List<ExamPaperAnswer> conditionQueryExamPaperAnswer(ExamPaperAnswer examPaperAnswer);

    ExamPaperAnswer findBySortMax(ExamPaperAnswer examPaperAnswer);

}
