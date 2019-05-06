package com.ynw.system.dao;

import com.ynw.system.entity.RadarSubject;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RadarSubjectMapper extends MyMapper<RadarSubject> {

    List<RadarSubject> conditionQueryRadarSubject(@Param("subject") RadarSubject subject, @Param("pageSize") Integer pageSize,
                                                  @Param("start") Integer start);

    Integer count(RadarSubject radarSubject);

    List<RadarSubject> findBySortMax(RadarSubject subject);

}
