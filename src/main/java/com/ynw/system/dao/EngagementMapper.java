package com.ynw.system.dao;

import com.ynw.system.entity.Engagement;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 约活动mapper层
 */
public interface EngagementMapper extends MyMapper<Engagement> {

    /**
     *  更新活动审核状态
     * @param engagement
     * @return
     */
    @Update("UPDATE t_at_register SET check_status = #{checkStatus} WHERE at_register_id = #{atRegisterId}")
    Integer updateCheckStatus(Engagement engagement);

    /**
     *  根据编号查询数据
     * @param register
     * @return
     */
    Engagement findById(String register);

    /**
     * 条件分页查询数据
     * @param engagement
     * @param pageSize
     * @param start
     * @return
     */
    List<Engagement> conditionQueryEngagement(@Param("engagement") Engagement engagement, @Param("pageSize") Integer pageSize,
                                              @Param("start") Integer start);

    /**
     * 条件查询数据总数
     * @param engagement
     * @return
     */
    Integer count(Engagement engagement);

}
