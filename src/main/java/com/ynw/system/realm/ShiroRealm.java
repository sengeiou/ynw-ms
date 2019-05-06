package com.ynw.system.realm;

import com.ynw.system.entity.Resource;
import com.ynw.system.entity.Role;
import com.ynw.system.entity.User;
import com.ynw.system.service.ResourceService;
import com.ynw.system.service.RoleService;
import com.ynw.system.service.UserService;
import com.ynw.system.util.MD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    /**
     * 授权
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        //获取用户信息
        User user= (User) SecurityUtils.getSubject().getPrincipal();//User{id=1, username='admin', password='3ef7164d1f6167cb9f2658c07d3c2f0a', enable=1}
        //获取权限
        List<Role> roles = roleService.findRoleByUserId(user.getMsUserId());
        // 权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for(Role role: roles){
            //添加角色
            info.addRole(role.getName());
            for (Resource resource: role.getResourceList()
                 ) {
                info.addStringPermission(resource.getSourceUrl());
            }
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        //获取用户和密码令牌
        UsernamePasswordToken token = (UsernamePasswordToken) arg0;
        String account = token.getUsername();
        //令牌中密码为密文，需要转换成字符串
        String password = new String(token.getPassword());
        //获取用户的输入的账号.
        User user1 = new User();
        user1.setName(account);
        User user = userService.selectOne(user1);
        //判断是否存在该账号
        if(null == user){
            throw new UnknownAccountException();
        }
        //判断账号是否有效
        if (user.getStatus() == 0)
            throw new LockedAccountException();
         SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                user,//用户
                user.getPassword(),//密码
                ByteSource.Util.bytes(user.getMsUserId()),//用户编号
                getName()//reaml name
        );

        //当验证都通过后，把用户信息放在session里
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("user", user);
        session.setAttribute("loginAccount", user.getMsUserId());

        return simpleAuthenticationInfo;
    }
}
