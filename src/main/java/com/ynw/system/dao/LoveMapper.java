package com.ynw.system.dao;

import com.ynw.system.entity.Love;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LoveMapper {

    List<Love> conditionQueryLove(@Param("love") Love love, @Param("beginDate") String beginDate, @Param("endDate") String endDate,
                                  @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer count(@Param("love") Love love, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    Integer nowCountGrant(String nowTime);

    Integer nowCountRecycle(String nowTime);

    Integer nowCountTransfer(String nowTime);

}
