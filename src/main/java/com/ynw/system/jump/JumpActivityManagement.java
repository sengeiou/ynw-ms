package com.ynw.system.jump;

import com.ynw.system.controller.UserController;
import com.ynw.system.entity.User;
import com.ynw.system.util.SetModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  @author ChengZhi
 *  @version 2019-1-19
 */
@Controller
@RequestMapping("ynw-ms/assets/activityManagement")
public class JumpActivityManagement {

    /**
     *  活动管理主页
     * @return
     */
    @RequestMapping("/index")
    public String jumpActivity(String name, String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, name);
        return "ynw-ms/assets/activityManagement/index";
    }

    /**
     *  undefined
     * @return
     */
    @RequestMapping("/undefined")
    public String jumpUndefined() {
        return "ynw-ms/assets/activityManagement/components/activity_management";
    }

    /**
     *  活动管理
     * @return
     */
    @RequestMapping("/components/activity_management")
    public String jumpActivityManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/activityManagement/components/activity_management";
    }

    /**
     *  查看内容详情
     * @return
     */
    @RequestMapping("/components/activity_content")
    public String jumpActivityContent(String atRegisterId, String resourceId, Model model) {
        SetModel.setModel(atRegisterId, resourceId, model);
        return "ynw-ms/assets/activityManagement/components/activity_content";
    }

    /**
     *  查看参与活动用户
     * @return
     */
    @RequestMapping("/components/active_user")
    public String jumpActiveUser(String atRegisterId, String resourceId, Model model) {
        SetModel.setModel(atRegisterId, resourceId, model);
        return "ynw-ms/assets/activityManagement/components/active_user";
    }

    /**
     *  查看活动打卡
     * @return
     */
    @RequestMapping("/components/active_task_clock")
    public String jumpActiveTaskClock(String atRegisterId, String resourceId, Model model) {
        SetModel.setModel(atRegisterId, resourceId, model);
        return "ynw-ms/assets/activityManagement/components/active_task_clock";
    }

    /**
     *  约活动管理
     * @param msResourceId
     * @param model
     * @return
     */
    @RequestMapping("/components/engagement_management")
    public String jumpEngagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/activityManagement/components/engagement_management";
    }

    /**
     *  约活动报名用户管理
     * @return
     */
    @RequestMapping("/components/invite_user")
    public String jumpInviteUser(String msResourceId, Model model, String parentId, String name, String userName, String phone, String atCtgyId,
                                 String atInviteThemeId, String checkStatus, String beginDate, Integer num, String registerId) {
        SetModel.setModel(msResourceId, model, parentId, name, userName, phone, atCtgyId, atInviteThemeId, checkStatus, beginDate, num);
        model.addAttribute("registerId", registerId);
        return "ynw-ms/assets/activityManagement/components/invite_user";
    }

    /**
     *  约活动主题管理
     * @return
     */
    @RequestMapping("/components/invite_theme")
    public String jumpInviteTheme(String msResourceId, Model model, String parentId, String name, String userName, String phone, String atCtgyId,
                                 String atInviteThemeId, String checkStatus, String beginDate, Integer num) {
        SetModel.setModel(msResourceId, model, parentId, name, userName, phone, atCtgyId, atInviteThemeId, checkStatus, beginDate, num);
        return "ynw-ms/assets/activityManagement/components/invite_theme";
    }

}
