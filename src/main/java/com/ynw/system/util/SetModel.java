package com.ynw.system.util;

import com.ynw.system.controller.UserController;
import com.ynw.system.entity.User;
import org.springframework.ui.Model;

/**
 *  model赋值
 */
public class SetModel {

    /**
     * model赋值
     * @param model
     */
    public static void setModel(String msResourceId, Model model, String parentId, String name, String userName, String phone, String atCtgyId,
                                String atInviteThemeId, String checkStatus, String beginDate, Integer num) {
        User user = UserController.userController.findUser();
        if (null != user) {
            model.addAttribute("msResourceId", msResourceId);
            model.addAttribute("msUserId", user.getMsUserId());
        }
        model.addAttribute("parentId", parentId);
        model.addAttribute("name", name);
        model.addAttribute("userName", userName);
        model.addAttribute("phone", phone);
        model.addAttribute("atCtgyId", atCtgyId);
        model.addAttribute("atInviteThemeId", atInviteThemeId);
        model.addAttribute("checkStatus", checkStatus);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("num", num);

    }

    /**
     * model赋值
     * @param model
     * @param msResourceId
     * @param resourceName
     */
    public static void setModel(Model model, String msResourceId, String resourceName) {
        User user = UserController.userController.findUser();
        if (null != user) {
            model.addAttribute("msResourceId", msResourceId);
            model.addAttribute("msUserId", user.getMsUserId());
            if (null != resourceName) {
                model.addAttribute("resourceName", resourceName);
            }
        }
    }

    public static void setModel(String parentId, String msResourceId, String name, String phone, String type, String beginDate,
                                String endDate, String withdrawDeposit, Integer num, Model model) {
        setModel(model, msResourceId, null);
        model.addAttribute("parentId", parentId);
        model.addAttribute("name", name);
        model.addAttribute("phone", phone);
        model.addAttribute("msResourceId", msResourceId);
        model.addAttribute("type", type);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("withdrawDeposit", withdrawDeposit);
        model.addAttribute("num", num);
    }

    public static void setModel(String atRegisterId, String msResourceId, Model model) {
        setModel(model, msResourceId, null);
        model.addAttribute("atRegisterId", atRegisterId);
    }

    public static void setModel( String msResourceId, Model model,String url, String resourceName) {
        if (null != url) {
            model.addAttribute("URL", url);
        }
        setModel(model, msResourceId, resourceName);
    }

    public static void setModel(Model model, String msResourceId, String url, String moodId) {
        setModel(model, msResourceId, null);
        if (null != url) {
            model.addAttribute("URL", url);
        }
        if (null != moodId) {
            model.addAttribute("moodId", moodId);
        }
    }

    public static void setModel(String targetId, String msResourceId, Model model, String name, String phone,
                                String syReportCtgyId, String targetType, Integer status, Integer num) {
        setModel(model, msResourceId, null, targetId);
        model.addAttribute("name", name);
        model.addAttribute("phone", phone);
        model.addAttribute("syReportCtgyId", syReportCtgyId);
        model.addAttribute("targetType", targetType);
        model.addAttribute("status", status);
        model.addAttribute("num", num);
    }

}
