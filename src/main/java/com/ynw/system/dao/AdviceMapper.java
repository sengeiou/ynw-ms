package com.ynw.system.dao;

import com.ynw.system.entity.Advice;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdviceMapper {

    List<Advice> conditionQueryAdvice(@Param("advice") Advice advice, @Param("pageSize") Integer pageSize,
                                      @Param("start") Integer start, @Param("reply") Integer reply);

    Integer count(@Param("advice") Advice advice, @Param("reply") Integer reply);

}
