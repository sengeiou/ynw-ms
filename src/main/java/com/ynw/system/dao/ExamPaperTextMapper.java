package com.ynw.system.dao;

import com.ynw.system.entity.ExamPaperTest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ExamPaperTextMapper {

    List<ExamPaperTest> conditionQueryExamPaperTest(@Param("examPaperTest") ExamPaperTest examPaperTest, @Param("start") Integer start,
                                                    @Param("pageSize") Integer pageSize);

    Integer count(ExamPaperTest examPaperTest);

    List<ExamPaperTest> findByUserId(String acUserId);

    Integer nowCount(String nowTime);

    Integer nowCountByUserId(String nowTime);

}
