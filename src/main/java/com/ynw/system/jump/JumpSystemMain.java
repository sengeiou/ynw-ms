package com.ynw.system.jump;

import com.ynw.system.controller.UserController;
import com.ynw.system.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class JumpSystemMain {

    @RequestMapping(value = "/ynw-ms/login", method = RequestMethod.GET)
    public String jumpLogin() {
        return "ynw-ms/transition";
    }

    @RequestMapping(value = "/ynw-ms/transition", method = RequestMethod.GET)
    public String jumpTransition() {
        return "ynw-ms/login";
    }

    @RequestMapping(value = "/ynw-ms/main", method = RequestMethod.GET)
    public String jumpIndex(Model model) {
        User user = UserController.userController.findUser();
        if (null != user) {
            model.addAttribute("name", user.getRealName());
            model.addAttribute("msUserId", user.getMsUserId());
        }
        return "ynw-ms/main";
    }

    /**
     *  退出登录
     * @return
     */
    @RequestMapping(value = "/ynw-ms/logout",  method = RequestMethod.GET)
    public String jumpLogout() {
        UserController.userController.logout();
        return "ynw-ms/login";
    }

    //被踢出后跳转的页面
        @RequestMapping(value = "/ynw-ms/kickout", method = RequestMethod.GET)
    public String kickOut() {
        return "ynw-ms/login";
    }

    @RequestMapping(value = "/ynw-ms/403", method = RequestMethod.GET)
    public String jumpNotRole() {
        return "/ynw-ms/403";
    }

}
