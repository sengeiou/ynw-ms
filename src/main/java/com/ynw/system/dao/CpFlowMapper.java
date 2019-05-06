package com.ynw.system.dao;

import com.ynw.system.entity.CpFlow;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CpFlowMapper extends MyMapper<CpFlow> {

    @Update("UPDATE t_ac_cp_rel SET `status`=#{status} WHERE src_id = #{registerId}")
    Integer updateStatusByRegisterId(@Param("registerId") String registerId, @Param("status") String status);

    Integer insertByBatch(List<CpFlow> list);

}
