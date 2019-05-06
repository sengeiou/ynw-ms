package com.ynw.system.dao;

import com.ynw.system.entity.ActiveTaskClockRegister;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ActiveTaskClockRegisterMapper {

    List<ActiveTaskClockRegister> conditionQueryActiveTaskClockRegister(@Param("register") ActiveTaskClockRegister register, @Param("pageSize") Integer pageSize,
                                                                        @Param("start") Integer start);

    Integer count(ActiveTaskClockRegister register);

}
