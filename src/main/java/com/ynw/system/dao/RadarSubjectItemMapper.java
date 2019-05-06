package com.ynw.system.dao;

import com.ynw.system.entity.RadarSubjectItem;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface RadarSubjectItemMapper extends MyMapper<RadarSubjectItem> {

    List<RadarSubjectItem> findAllBySubjectId(String subjectId);

    List<RadarSubjectItem> findBySortMax(String subjectId);

}
