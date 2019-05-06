package com.ynw.system.dao;

import com.ynw.system.entity.Register;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegisterMapper extends MyMapper<Register> {

    List<Register> conditionQueryRegister(@Param("register") Register register, @Param("pageSize") Integer pageSize,
                                          @Param("start") Integer start, @Param("status") Integer status);

    Integer count(@Param("register") Register register, @Param("status") Integer status);

}
