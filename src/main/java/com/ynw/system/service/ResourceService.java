package com.ynw.system.service;

import com.sun.org.apache.regexp.internal.RE;
import com.ynw.system.dao.ResourceMapper;
import com.ynw.system.dao.UserMapper;
import com.ynw.system.entity.Resource;
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
public class ResourceService extends MyService<ResourceMapper, Resource> {

    /**
     *  根据账号查询资源
     * @param msUserId 账号编号
     * @return
     */
//    @Cacheable("findByUser")
    @Transactional(readOnly = true)
    public List<Resource> findResourceByUserId(String msUserId) {
        return dao.findResourceByUserId(msUserId);
    }

    /**
     *  查询所有
     * @return
     */
//    @Cacheable("findAll")
    @Transactional(readOnly = true)
    public List<Resource> findAll() {
        return  dao.findAll();
    }

    /**
     *  条件查询管理员的资源
     * @param msUserId 管理员账号
     * @param resource
     * @return
     */
//    @Cacheable("conditionQuery")
    @Transactional(readOnly = true)
    public List<Resource> conditionQueryResourceByUserId(String msUserId, Resource resource) {
        return dao.conditionQueryResourceByUserId(msUserId, resource);
    }

    /**
     *  条件查询资源
     * @param resource
     * @param start
     * @param pageSize
     * @return
     */
//    @Cacheable("conditionQueryAll")
    @Transactional(readOnly = true)
    public List<Resource> conditionQueryResourceAll(Resource resource, Integer start, Integer pageSize) {
        return dao.conditionQueryResourceAll(resource, start, pageSize);
    }

    /**
     *  统计数据
     * @param resource
     * @return
     */
//    @Cacheable("count")
    @Transactional(readOnly = true)
    public Integer count(Resource resource) {
        return dao.count(resource);
    }

    /**
     *  查询目录下的所有子集
     * @return
     */
    @Transactional(readOnly = true)
    public List<Resource> relevanceFindResource() {
        return dao.relevanceFindResource();
    }

    /**
     * 查询角色下的资源
     * @param roleId 角色编号
     * @return
     */
    @Transactional(readOnly = true)
    public List<Resource> findResourceByRoleId(String roleId) {
        return dao.findResourceByRoleId(roleId);
    }

    /**
     *  获取type为菜单和目录的资源
     * @return
     */
    public List<Resource> findByTypeLessThanOne() {
        return dao.findByTypeLessThanOne();
    }

}
