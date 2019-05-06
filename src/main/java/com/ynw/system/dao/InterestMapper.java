package com.ynw.system.dao;

import com.ynw.system.entity.Interest;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InterestMapper extends MyMapper<Interest> {

    List<Interest> conditionQueryInterest(@Param("interest") Interest interest, @Param("start") Integer start,
                                          @Param("pageSize") Integer pageSize);

    Integer count(Interest interest);

    List<Interest> findBySortMax();

    List<Interest> findInterestByAcUserId(String acUserId);

}
