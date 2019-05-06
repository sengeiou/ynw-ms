package com.ynw.system.dao;

import com.ynw.system.entity.SweetsFlow;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SweetsFlowMapper extends MyMapper<SweetsFlow> {

    /**
     * 根据id查询数据
     * @param acSugarFlowId
     * @return
     */
    SweetsFlow findById(String acSugarFlowId);

    /**
     *  条件分页查询数据
     * @param sweetsFlow 查询对象条件
     * @param beginDate 查询开始时间
     * @param endDate 查询介绍时间
     * @param start
     * @param pageSize
     * @return
     */
    List<SweetsFlow> conditionQuerySweetsFlow(@Param("sweetsFlow") SweetsFlow sweetsFlow, @Param("beginDate") String beginDate, @Param("endDate") String endDate,
                                  @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    /**
     *  符合条件的总数
     * @param sweetsFlow
     * @param beginDate
     * @param endDate
     * @return
     */
    Integer count(@Param("sweetsFlow") SweetsFlow sweetsFlow, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

}
