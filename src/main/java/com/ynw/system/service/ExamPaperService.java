package com.ynw.system.service;

import com.ynw.system.dao.ExamPaperMapper;
import com.ynw.system.dao.ReportMapper;
import com.ynw.system.entity.ExamPaper;
import com.ynw.system.entity.Report;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/13
 */
@Service
@Transactional
public class ExamPaperService extends MyService<ExamPaperMapper, ExamPaper> {

    /**
     *  条件查询问卷
     * @param examPaper
     * @return
     */
    public List<ExamPaper> conditionQueryExamPaper(ExamPaper examPaper, Integer start, Integer pageSize) {
        return dao.conditionQueryExamPaper(examPaper, start, pageSize);
    }

    /**
     *  条件查询数据总数
     * @param examPaper
     * @return
     */
    public Integer count(ExamPaper examPaper) {
        return dao.count(examPaper);
    }

    /**
     *  获取排序最大的数据
     * @return
     */
    public ExamPaper findBySortMax() {
        return dao.findBySortMax();
    }

}
