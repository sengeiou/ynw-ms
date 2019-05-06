package com.ynw.system.dao;

import com.ynw.system.entity.Report;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReportMapper extends MyMapper<Report> {

    List<Report> conditionQueryReport(@Param("report") Report report, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer count(Report report);

    List<Report> findReportTarget(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("name") String name,@Param("phone")  String phone);

    Integer countReportTarget(@Param("name") String name,@Param("phone")  String phone);

    List<Report> findReportByTarget(String targetId);

}
