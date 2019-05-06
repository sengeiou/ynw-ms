package com.ynw.system.dao;

import com.ynw.system.entity.Role;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMapper extends MyMapper<Role> {

    List<Role> findRoleByUserId(String msUserId);

    List<Role> conditionQueryRole(@Param("role") Role role, @Param("start") Integer start,
                                             @Param("pageSize") Integer pageSize);

    Integer count(Role role);

    List<Role> findAll();

    Role findById(String roleId);

}
