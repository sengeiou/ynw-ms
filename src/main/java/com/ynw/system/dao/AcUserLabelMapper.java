package com.ynw.system.dao;

import com.ynw.system.entity.AcUserLabel;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AcUserLabelMapper extends MyMapper<AcUserLabel> {

    List<AcUserLabel> conditionQueryAcUserLabel(@Param("label") AcUserLabel label, @Param("pageSize") Integer pageSize, @Param("start") Integer start);

    Integer count(AcUserLabel label);

    List<AcUserLabel> findBySortMax(String labelClassifyId);

    List<AcUserLabel> findAllByUserId(String userId);

}
