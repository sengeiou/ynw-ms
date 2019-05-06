package com.ynw.system.service;

import com.ynw.system.dao.ExamPaperAnswerFlowMapper;
import com.ynw.system.entity.ExamPaperAnswerFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/18
 */
@Service
@Transactional
public class ExamPaperAnswerFlowService {

    @Autowired
    private ExamPaperAnswerFlowMapper examPaperAnswerFlowMapper;

    /**
     *  根据测试编号查询所有流水记录
     * @param kbExampaperTestId
     * @return
     */
    public List<ExamPaperAnswerFlow> findByExamId(String kbExampaperTestId) {
        return examPaperAnswerFlowMapper.findByExamId(kbExampaperTestId);
    }

}
