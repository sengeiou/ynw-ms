package com.ynw.system.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.ynw.system.entity.User;
import com.ynw.system.entity.UserAndRole;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ResourceAndRoleService;
import com.ynw.system.service.UserAndRoleService;
import com.ynw.system.service.UserService;
import com.ynw.system.util.MD5Util;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import com.ynw.system.util.UUIDUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ynw-ms/admin")
@Api(tags = "管理员接口",description = "管理员查询及操作接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserAndRoleService userAndRoleService;
    @Autowired
    private ResourceAndRoleService resourceAndRoleService;

    public static UserController userController;
    //调用service层初始化(防止外部调用时报空)
    @PostConstruct
    public void init() {
        userController = this;
        userController.userService = this.userService;
    }

    @PostMapping("/login")
    @ApiOperation(value = "登录", notes = "传入：账号（name)(必传),密码（password)(必传)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "账号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> login(String name, String password) {
        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(password)) {
            //创建登陆令牌
            UsernamePasswordToken token = new UsernamePasswordToken(name, password);
            //创建一个登陆的用户
            Subject subject = SecurityUtils.getSubject();
            //用户登陆
            try {
                subject.login(token);
                User user = (User) subject.getSession().getAttribute("user");
                user.setLastLoginTime(new Date());
                user.setLoginNum(user.getLoginNum() + 1);
                userService.update(user);
                //登录成功设置session时间（客户端）
                subject.getSession().setTimeout(60*60*1000L);
                return ResultUtil.successResponse();
            } catch (LockedAccountException e) {
                token.clear();
                return ResultUtil.errorResponse(ResultEnums.LOCK_ERROR);
            } catch (AuthenticationException e) {
                token.clear();
                return ResultUtil.errorResponse(ResultEnums.ACC_PASS_ERROR);
            }
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改管理员信息
     * @param user
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改管理员信息", notes = "传入：账户ID（msUserId)，账户密码（password），账户名（name）" +
            ",真实姓名（realName)，手机号码（phoneNumber），性别（sex），邮箱（email）" +
            "，添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(User user) {
        User user1 = userService.selectOne(new User(user.getMsUserId()));
        if (null == user1) {
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        MD5Util.encryptPassword(user);
        user1.setName(user.getName());
        user1.setPassword(user.getPassword());
        user1.setPhoneNumber(user.getPhoneNumber());
        user1.setRealName(user.getRealName());
        user1.setEmail(user.getEmail());
        user1.setSex(user.getSex());
        if (userService.update(user1) > 0) {
            return ResultUtil.successResponse();
        }
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  修改管理员密码为123456
     * @param user 管理员账号
     * @return
     */
    @PostMapping("/updatePassword")
    @ApiOperation(value = "修改管理员密码为123456", notes = "传入：账户ID（msUserId)，" +
            "，添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> updatePassword(User user) {
        if (StringUtils.isEmpty(user.getMsUserId())) {
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        User users = userService.selectOne(user);
        if (null != user) {
            users.setPassword("123456");
            MD5Util.encryptPassword(users);
            if (userService.update(users) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  修改管理员账号状态
     * @param userId
     * @return
     */
    @PostMapping("/updateUserStatus")
    @ApiOperation(value = "修改管理员账号状态", notes = "传入：管理员账号（userId)(必传)")
    @ApiImplicitParam(name = "userId", value = "管理员账号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> updateUserStatus(String userId) {
        if (StringUtils.isEmpty(userId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        User user = userService.selectOne(new User(userId));
        if (null == user)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        if (user.getStatus() == 1) {
            user.setStatus(0);
        } else if (user.getStatus() == 0) {
            user.setStatus(1);
        }
        if (userService.update(user) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  根据编号删除数据
     * @param user
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "根据编号删除数据", notes = "传入：账户ID（msUserId)，" +
            "，添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(User user) {
        if (StringUtils.isNotEmpty(user.getMsUserId())) {
            if (userService.deleteUser(user) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加管理员
     * @param user
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "修改管理员信息", notes = "传入：账户密码（password），账户名（name）" +
            ",真实姓名（realName)，手机号码（phoneNumber），性别（sex），邮箱（email）,角色编号(roleId)" +
            "，添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(User user) throws Exception {
        if (StringUtils.isNotEmpty(user.getPassword()) && StringUtils.isNotEmpty(user.getName())
                && StringUtils.isNotEmpty(user.getRealName()) && StringUtils.isNotEmpty(user.getPhoneNumber()) && null != user.getSex()) {
            user.setMsUserId(UUIDUtil.getEUUID());
            MD5Util.encryptPassword(user);
            if (userService.insertUser(user) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  根据编号查询管理员
     * @param user
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据编号查询管理员", notes = "传入：账户ID（msUserId)")
    public ResponseEntity<Result> findById(User user) {
        if (StringUtils.isNotEmpty(user.getMsUserId())) {
            User user1 = userService.selectOne(user);
            if (null != user1) {
                return ResultUtil.successResponse(user1);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加/更新管理员角色
     * @param userAndRole
     * @return
     */
    @PostMapping("/insertUserAndRole")
    @ApiOperation(value = "添加/更新管理员角色", notes = "传入：账户ID（msUserId)(必传),角色ID（msRoleId)(必传)")
    public ResponseEntity<Result> insertUserAndRole(UserAndRole userAndRole) {
        if (StringUtils.isNotEmpty(userAndRole.getMsRoleId()) && StringUtils.isNotEmpty(userAndRole.getMsUserId())) {
            UserAndRole userAndRole2 = new UserAndRole();
            userAndRole2.setMsUserId(userAndRole.getMsUserId());
            UserAndRole userAndRole1 = userAndRoleService.selectOne(userAndRole2);
            if (null == userAndRole1) {
                //未查到添加
                if (userAndRoleService.insert(userAndRole) > 0) {
                    return ResultUtil.successResponse();
                }
            } else {
                //查到更新
                userAndRole1.setMsRoleId(userAndRole.getMsRoleId());
                if (userAndRoleService.update(userAndRole1) > 0) {
                    return ResultUtil.successResponse();
                }
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  角色的资源操作
     * @param roleId
     * @param resourceIds
     * @return
     */
    @PostMapping("/operationResourceAndRole")
    @ApiOperation(value = "角色的资源操作", notes = "传入：角色编号（roleId)(必传)，资源集合（多个资源用'，'隔开）（resourceIds)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色编号", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "resourceIds", value = "资源集合", dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> operationResourceAndRole(String roleId,String resourceIds) {
        if (StringUtils.isNotEmpty(roleId)) {
            if (resourceAndRoleService.operationResourceAndRole(roleId, resourceIds) == 1) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  获取全部管理员
     * @return
     */
    @PostMapping("/selectAll")
    @ApiOperation(value = "获取全部管理员")
    public ResponseEntity<Result> selectAll() {
        return ResultUtil.successResponse(userService.selectAll());
    }

    /**
     *  条件查询管理员
     * @param user
     * @return
     */
    @PostMapping("/conditionQueryUserAll")
    @ApiOperation(value = "条件查询管理员", notes = "传入：页码（nowPage必传），账户名（name），角色编号（roleId）" +
            ",手机号码（phoneNumber），状态（status）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryUserAll(User user, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            if (StringUtils.isEmpty(user.getRoleId())) {
                user.setRoleId(null);
            }
            User user1 = findUser();
            if (!(user1.getMsUserId().equals("c73a9f42b17a4ce20181227170135651"))) {
                user.setMsUserId(user1.getMsUserId());
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<User> userList = userService.conditionQueryUserAll(user, start, pageSize);
            if (userList.size() > 0) {
                return ResultUtil.successResponse(userList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件统计数量
     * @param user
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询管理员", notes = "传入：账户名（name），角色编号（roleId）" +
            ",手机号码（phoneNumber），状态（status）")
    public ResponseEntity<Result> count(User user) {
        if (StringUtils.isEmpty(user.getRoleId())) {
            user.setRoleId(null);
        }
        User user1 = findUser();
        if (!(user1.getMsUserId().equals("c73a9f42b17a4ce20181227170135651"))) {
            user.setMsUserId(user1.getMsUserId());
        }
        Integer count = userService.count(user);
        if (count > 0) {
            return ResultUtil.successResponse(count);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  获取登录对象
     * @return
     */
    public User findUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getSession().getAttribute("user");
    }

    /**
     *  退出登录
     */
    public void logout() {
        // 登出操作
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

}
