package com.ynw.system.dao;

import com.ynw.system.entity.ExamPaperAnswerFlow;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExamPaperAnswerFlowMapper {

    List<ExamPaperAnswerFlow> findByExamId(String kbExampaperTestId);

}
