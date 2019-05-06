package com.ynw.system.service;

import com.ynw.system.dao.UserMapper;
import com.ynw.system.entity.Role;
import com.ynw.system.entity.User;
import com.ynw.system.entity.UserAndRole;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserService extends MyService<UserMapper, User> {

    @Autowired
    private UserAndRoleService userAndRoleService;

    /**
     *  添加管理员
     * @param user
     * @return
     */
    public Integer insertUser(User user) {
        user.preInsert();
        Integer code = dao.insertUser(user);
        if (code > 0) {
            if (StringUtils.isNotEmpty(user.getRoleId())) {
                UserAndRole userAndRole = new UserAndRole();
                userAndRole.setMsRoleId(user.getRoleId());
                userAndRole.setMsUserId(user.getMsUserId());
                code = userAndRoleService.insert(userAndRole);
                if (code < 1) {
                    throw new MyException(ResultEnums.ADDITION_FAILED);
                }
            }
        }
        return code;
    }

    /**
     *  删除管理员与角色以及管理员信息
     * @param user
     * @return
     */
    public Integer deleteUser(User user) {
        Integer code = 1;
        UserAndRole userAndRole = userAndRoleService.selectOne(new UserAndRole(user.getMsUserId()));
        if (null != userAndRole) {
            code = userAndRoleService.delete(userAndRole);
        }
        if (code > 0) {
            code = dao.delete(user);
        }
        if (code < 1) {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

//    /**
//     *  根据编号查询管理员
//     * @param user
//     * @return
//     */
//    @Cacheable("findByUserId")
//    @Transactional(readOnly = true)
//    public User findById(User user) {
//        return dao.findById(user);
//    }

    /**
     *  条件查询管理员
     * @param user
     * @return
     */
//    @Cacheable("conditionQueryAll")
    @Transactional(readOnly = true)
    public List<User> conditionQueryUserAll(User user, Integer start, Integer pageSize) {
        return dao.conditionQueryUserAll(user, start, pageSize);
    }

    /**
     *  根据角色编号查询管理员
     * @param user
     * @return
     */
//    @Cacheable("findByUser")
    @Transactional(readOnly = true)
    public List<User> findUserByRoleId(User user) {
        return dao.findUserByRoleId(user);
    }

    /**
     *  根据条件统计数量
     * @param user
     * @return
     */
//    @Cacheable("count")
    @Transactional(readOnly = true)
    public Integer count(User user) {
        return dao.count(user);
    }

    /**
     *  获取除超级管理员外所有账户
     * @return
     */
    public List<User> findAll() {
        return dao.findAll();
    }

}
