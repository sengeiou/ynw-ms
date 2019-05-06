package com.ynw.system.jump;

import com.ynw.system.controller.UserController;
import com.ynw.system.entity.User;
import com.ynw.system.util.SetModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  @author ChengZhi
 *  @version 2018-11-27
 */
@Controller
@RequestMapping("ynw-ms/assets/authorityManagement")
public class JumpAuthorityManagement {

    /**
     *  权限管理主页
     * @return
     */
    @RequestMapping("/index")
    public String jumpLogin(String name, String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, name);
        return "ynw-ms/assets/authorityManagement/index";
    }

    /**
     *  undefined
     * @return
     */
    @RequestMapping("/undefined")
    public String jumpUndefined() {
        return "ynw-ms/assets/authorityManagement/undefined";
    }

    /**
     *  管理员管理主页
     * @return
     */
    @RequestMapping("/components/admin_management")
    public String jumpAdminManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/authorityManagement/components/admin_management";
    }

    /**
     *  管理员角色管理主页
     * @return
     */
    @RequestMapping("/components/role_management")
    public String jumpRoleManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/authorityManagement/components/role_management";
    }

    /**
     *  管理员资源管理管理主页
     * @return
     */
    @RequestMapping("/components/resource_management")
    public String jumpResourceManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/authorityManagement/components/resource_management";
    }

}
