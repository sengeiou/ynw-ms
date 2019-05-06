package com.ynw.system.dao;

import com.ynw.system.entity.ReportType;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface ReportTypeMapper extends MyMapper<ReportType> {

    List<ReportType> findAll();

    List<ReportType> findBySortMax();

}
