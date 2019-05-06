package com.ynw.system.dao;

import com.ynw.system.entity.Resource;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceMapper extends MyMapper<Resource> {

    List<Resource> findResourceByUserId(String msUserId);

    List<Resource> findAll();

    List<Resource> conditionQueryResourceByUserId(@Param("msUserId") String msUserId, @Param("resource") Resource resource);

    List<Resource> conditionQueryResourceAll(@Param("resource") Resource resource, @Param("start") Integer start,
                                             @Param("pageSize") Integer pageSize);

    Integer count(Resource resource);

    List<Resource> relevanceFindResource();

    List<Resource> findResourceByRoleId(String roleId);

    List<Resource> findByTypeLessThanOne();

}
