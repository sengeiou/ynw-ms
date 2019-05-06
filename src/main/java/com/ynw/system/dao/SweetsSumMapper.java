package com.ynw.system.dao;

import com.ynw.system.entity.SweetsSum;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SweetsSumMapper extends MyMapper<SweetsSum> {

    /**
     * 根据用户编号更新提现金额
     * @param sweetsSum
     * @return
     */
    Integer updateSumAndMoney(SweetsSum sweetsSum);

    /**
     * 根据用户编号更新糖果总量
     * @param sweetsSum
     * @return
     */
    Integer updateSum(SweetsSum sweetsSum);

    /**
     *  条件分页查询数据
     * @param sweetsSum 查询对象条件
     * @param start
     * @param pageSize
     * @return
     */
    List<SweetsSum> conditionQuerySweetsSum(@Param("sweetsSum") SweetsSum sweetsSum,
                                             @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    /**
     *  符合条件的总数
     * @param sweetsSum
     * @return
     */
    Integer count(SweetsSum sweetsSum);

}
