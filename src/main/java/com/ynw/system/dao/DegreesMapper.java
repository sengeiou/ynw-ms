package com.ynw.system.dao;

import com.ynw.system.entity.Degrees;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DegreesMapper extends MyMapper<Degrees> {

    List<Degrees> conditionQueryDegrees(@Param("degrees") Degrees degrees, @Param("start") Integer start,
                                          @Param("pageSize") Integer pageSize);

    Integer count(Degrees degrees);

    List<Degrees> findBySortMax();

}
