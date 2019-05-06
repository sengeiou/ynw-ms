package com.ynw.system.service;

import com.ynw.system.dao.ExamPaperAnswerMapper;
import com.ynw.system.entity.ExamPaperAnswer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/15
 */
@Service
@Transactional
public class ExamPaperAnswerService extends MyService<ExamPaperAnswerMapper, ExamPaperAnswer> {

    /**
     * 根据问卷查询答案
     * @param examPaperAnswer
     * @return
     */
    public List<ExamPaperAnswer> conditionQueryExamPaperAnswer(ExamPaperAnswer examPaperAnswer) {
        return dao.conditionQueryExamPaperAnswer(examPaperAnswer);
    }

    /**
     *  根据问卷获取降序数据
     * @return
     */
    public ExamPaperAnswer findBySortMax(ExamPaperAnswer examPaperAnswer) {
        return dao.findBySortMax(examPaperAnswer);
    }

}
