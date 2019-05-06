package com.ynw.system.service;

import com.ynw.system.dao.ExamPaperTextMapper;
import com.ynw.system.entity.ExamPaperTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/14
 */
@Service
@Transactional
public class ExamPaperTestService {

    @Autowired
    private ExamPaperTextMapper examPaperTextMapper;

    /**
     *  条件查询问卷测试
     * @param examPaperTest
     * @return
     */
    public List<ExamPaperTest> conditionQueryExamPaperTest(ExamPaperTest examPaperTest, Integer start, Integer pageSize) {
        return examPaperTextMapper.conditionQueryExamPaperTest(examPaperTest, start, pageSize);
    }

    /**
     *  条件查询数据总数
     * @param examPaperTest
     * @return
     */
    public Integer count(ExamPaperTest examPaperTest) {
        return examPaperTextMapper.count(examPaperTest);
    }

    /**
     *  根据用户编号查询测试
     * @param acUserId
     * @return
     */
    public List<ExamPaperTest> findByUserId(String acUserId) {
        return examPaperTextMapper.findByUserId(acUserId);
    }

    /**
     *  查询当天测试数
     * @param nowTime
     * @return
     */
    public Integer nowCount(String nowTime) {
        return examPaperTextMapper.nowCount(nowTime);
    }

    /**
     *  查询当天考试人数
     * @param nowTime
     * @return
     */
    public Integer nowCountByUserId(String nowTime) {
        return examPaperTextMapper.nowCountByUserId(nowTime);
    }

}
