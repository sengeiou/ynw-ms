package com.ynw.system.service;

import com.ynw.system.dao.RoleMapper;
import com.ynw.system.dao.UserMapper;
import com.ynw.system.entity.Role;
import com.ynw.system.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-11-24
 */
@Service
@Transactional
public class RoleService extends MyService<RoleMapper, Role> {

    /**
     * 根据用户编号查询自己的角色和资源
     * @param msUserId
     * @return
     */
//    @Cacheable("findByUser")
    @Transactional(readOnly = true)
    public List<Role> findRoleByUserId(String msUserId) {
        return dao.findRoleByUserId(msUserId);
    }

    /**
     *  条件查询角色
     * @param role
     * @param start
     * @param pageSize
     * @return
     */
//    @Cacheable("conditionQuery")
    @Transactional(readOnly = true)
    public List<Role> conditionQueryRole(Role role, Integer start, Integer pageSize) {
        return dao.conditionQueryRole(role, start, pageSize);
    }

    /**
     *  统计数据
     * @param role
     * @return
     */
//    @Cacheable("count")
    @Transactional(readOnly = true)
    public Integer count(Role role) {
        return dao.count(role);
    }

    /**
     *  查询出超级管理员之外的角色
     * @return
     */
    public List<Role> findAll() {
        return dao.findAll();
    }

    /**
     *  根据编号查询角色信息
     * @param roleId
     * @return
     */
    public Role findById(String roleId) {
        return dao.findById(roleId);
    }

}
