package com.ynw.system.controller;

import com.ynw.system.entity.Role;
import com.ynw.system.entity.User;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.RoleService;
import com.ynw.system.service.UserService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ynw-ms/role")
@Api(tags = "管理员角色接口",description = "管理员角色查询及操作接口")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    /**
     *  添加角色信息
     * @param role
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加角色信息", notes = "传入：角色名称（name），角色Key（roleKey），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Role role) {
        if (StringUtils.isNotEmpty(role.getName()) && StringUtils.isNotEmpty(role.getRoleKey())) {
            if (roleService.insert(role) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改角色信息
     * @param role
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改角色信息", notes = "传入：角色ID（msRoleId），角色名称（name），角色Key（roleKey），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Role role) {
        if (StringUtils.isNotEmpty(role.getRoleKey()) && StringUtils.isNotEmpty(role.getName()) && StringUtils.isNotEmpty(role.getMsRoleId())) {
            Role role1 = roleService.selectOne(new Role(role.getMsRoleId()));
            if (null != role1) {
                role1.setName(role.getName());
                role1.setRoleKey(role.getRoleKey());
                if (roleService.update(role1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除角色信息
     * @param role
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除角色信息", notes = "传入：角色ID（msRoleId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Role role) {
        if (StringUtils.isNotEmpty(role.getMsRoleId())) {
            if (roleService.delete(role) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

//    @PostMapping("/findRoleByUserId")
//    public ResponseEntity<Result> findRoleByUserId(String msUserId) {
//        List<Role> roles = roleService.findRoleByUserId(msUserId);
//        return ResultUtil.successResponse(roles);
//    }

    /**
     *  条件查询管理员角色
     * @param role
     * @return
     */
    @PostMapping("/conditionQueryRoleAll")
    @ApiOperation(value = "条件查询管理员角色", notes = "传入：页码（nowPage必传），角色名称（name），角色Key（roleKey），")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryUserAll(Role role, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            User user1 = UserController.userController.findUser();
            if (!(user1.getMsUserId().equals("c73a9f42b17a4ce20181227170135651"))) {
                //暂时存储，方便查询时调用
                role.setMsRoleId(user1.getMsUserId());
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Role> roleList = roleService.conditionQueryRole(role, start, pageSize);
            if (roleList.size() > 0) {
                User user = new User();
                for (Role roles: roleList
                ) {
                    user.setRoleId(roles.getMsRoleId());
                    roles.setUserList(userService.findUserByRoleId(user));
                }
                return ResultUtil.successResponse(roleList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  查询所有角色
     * @return
     */
    @PostMapping("/findAll")
    @ApiOperation(value = "查询所有角色")
    public ResponseEntity<Result> findAll() {
        List<Role> roles = roleService.findAll();
        if (roles.size() > 0) {
            return ResultUtil.successResponse(roles);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据编号查询角色信息
     * @param roleId
     * @return
     */
    @PostMapping("/findUserByRoleId")
    @ApiOperation(value = "根据编号查询角色信息", notes = "传入：角色编号（roleId)(必传)")
    @ApiImplicitParam(name = "roleId", value = "角色编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findById(String roleId) {
        if (StringUtils.isEmpty(roleId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        User user = new User();
        user.setRoleId(roleId);
        List<User> userList = userService.findUserByRoleId(user);
        if (userList.size() > 0)
            return ResultUtil.successResponse(userList);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  条件统计数据
     * @param role
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件统计数据", notes = "传入：角色名称（name），角色Key（roleKey），")
    public ResponseEntity<Result> count(Role role) {
        User user1 = UserController.userController.findUser();
        if (!(user1.getMsUserId().equals("c73a9f42b17a4ce20181227170135651"))) {
            //暂时存储，方便查询时调用
            role.setMsRoleId(user1.getMsUserId());
        }
        Integer count = roleService.count(role);
        return ResultUtil.successResponse(count);
    }

}
