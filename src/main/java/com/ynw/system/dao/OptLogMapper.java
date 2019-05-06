package com.ynw.system.dao;

import com.ynw.system.entity.OptLog;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OptLogMapper extends MyMapper<OptLog> {

    List<OptLog> conditionQueryOptLog(@Param("optLog") OptLog optLog, @Param("beginDate") String beginDate, @Param("endDate") String endDate,
                                      @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer count(@Param("optLog") OptLog optLog, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

}
