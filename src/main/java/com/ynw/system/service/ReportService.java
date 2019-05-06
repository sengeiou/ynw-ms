package com.ynw.system.service;

import com.ynw.system.dao.ReportMapper;
import com.ynw.system.entity.Report;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/7
 */
@Service
@Transactional
public class ReportService extends MyService<ReportMapper, Report> {

    /**
     *  条件查询举报信息
     * @param report
     * @return
     */
    public List<Report> conditionQueryReport(Report report, Integer start, Integer pageSize) {
        return dao.conditionQueryReport(report, start, pageSize);
    }

    /**
     *  条件查询数据总数
     * @param report
     * @return
     */
    public Integer count(Report report) {
        return dao.count(report);
    }

    /**
     *  查询举报对象数据
     * @param start
     * @param pageSize
     * @return
     */
    public List<Report> findReportTarget(Integer start, Integer pageSize, String name, String phone) {
        return dao.findReportTarget(start, pageSize, name, phone);
    }

    /**
     *  统计举报数据
     * @return
     */
    public Integer countReportTarget(String name, String phone) {
        return dao.countReportTarget(name, phone);
    }

    /**
     *  根据举报对象编号查询数据
     * @param targetId
     * @return
     */
    public List<Report> findReportByTarget(String targetId) {
        return dao.findReportByTarget(targetId);
    }

}
