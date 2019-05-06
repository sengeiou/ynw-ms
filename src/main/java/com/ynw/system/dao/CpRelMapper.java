package com.ynw.system.dao;

import com.ynw.system.entity.CpRel;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public interface CpRelMapper extends MyMapper<CpRel> {

    CpRel findRelByPhone(String phone);

    List<CpRel> findCpRelByUserIdOrStatus(@Param("userId") String userId,@Param("status") String status);

    @Update("UPDATE t_ac_cp_rel SET `status`=#{status} WHERE src_id = #{registerId}")
    Integer updateStatusByRegisterId(@Param("registerId") String registerId,@Param("status") String status);

    @Update("UPDATE t_ac_cp_rel SET `status`=#{status},create_time=#{date} WHERE ac_cp_rel_id = #{acCpRelId}")
    Integer updateStatusByAcCpRelId(@Param("acCpRelId") String acCpRelId, @Param("status") String status, @Param("date") Date date);

    Integer batchUpdateStatus(@Param("list") List<CpRel> list, @Param("status") String status);

    List<CpRel> findByRegisterIdOrStatus(String registerId);

    CpRel findByReqUserIdAndResUserIdAndRegisterId(@Param("reqUserId") String reqUserId, @Param("resUserId") String resUserId, @Param("registerId") String registerId);

    CpRel findByReqUserIdAndResUserId(@Param("reqUserId") String reqUserId, @Param("resUserId") String resUserId);

    Integer insertByBatch(List<CpRel> list);

    /**
     *  根据用户活动是否响应更新cp关系
     * @return
     */
    Integer updateByConfirmCp(String registerId);

    /**
     *  将当期活动下除开cp关系外全部更新为cp关系删除
     * @param registerId
     * @return
     */
    Integer updateByRegisterIdOrStatus(String registerId);

    /**
     * 根据活动编号和状态查询是cp的数据
     * @param registerId
     * @return
     */
    List<CpRel> findByRegisterIdAndStatus(String registerId);

}
