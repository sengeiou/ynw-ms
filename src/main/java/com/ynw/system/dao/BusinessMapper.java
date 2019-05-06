package com.ynw.system.dao;

import com.ynw.system.entity.Business;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusinessMapper extends MyMapper<Business> {

    List<Business> conditionQueryBusiness(@Param("business") Business business, @Param("start") Integer start,
                                          @Param("pageSize") Integer pageSize);

    Integer count(Business business);

    List<Business> findBySortMax();

}
