package com.ynw.system.dao;

import com.ynw.system.entity.User;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper extends MyMapper<User> {

    User findById(User user);

    List<User> conditionQueryUserAll(@Param("user") User user, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    List<User> findUserByRoleId(User user);

    Integer count(User user);

    List<User> findAll();

    Integer insertUser(User user);
}
