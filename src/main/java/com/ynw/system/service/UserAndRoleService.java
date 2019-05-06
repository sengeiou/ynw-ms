package com.ynw.system.service;

import com.ynw.system.dao.RoleMapper;
import com.ynw.system.dao.UserAndRoleMapper;
import com.ynw.system.entity.Role;
import com.ynw.system.entity.UserAndRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  @author ChengZhi
 *  @version 2018-11-27
 */
@Service
@Transactional
public class UserAndRoleService extends MyService<UserAndRoleMapper, UserAndRole> {
}
