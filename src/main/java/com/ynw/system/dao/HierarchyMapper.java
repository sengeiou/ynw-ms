package com.ynw.system.dao;

import com.ynw.system.entity.Hierarchy;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HierarchyMapper extends MyMapper<Hierarchy> {

    List<Hierarchy> conditionQueryHierarchy(@Param("hierarchy") Hierarchy hierarchy, @Param("pageSize") Integer pageSize,
                                            @Param("start") Integer start);

    Integer count(Hierarchy hierarchy);

    List<Hierarchy> findByNoMax();

}
