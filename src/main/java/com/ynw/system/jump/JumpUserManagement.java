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
@RequestMapping("ynw-ms/assets/userManagement")
public class JumpUserManagement {

    @RequestMapping("/index")
    public String jumpIndex(String name, String msResourceId, Model model) {
//        setModel(model, msResourceId,null,name);
        SetModel.setModel(msResourceId, model, null, name);
        return "ynw-ms/assets/userManagement/index";
    }

    /**
     *  undefined
     * @return
     */
    @RequestMapping("/undefined")
    public String jumpUndefined() {
        return "ynw-ms/assets/userManagement/undefined";
    }

    /**
     *  用户信息管理
     * @return
     */
    @RequestMapping("/components/user_info_management")
    public String jumpParameterManagement(String msResourceId, Model model) {
        SetModel.setModel(msResourceId, model, "assets/userManagement/components/user_info_management", null);
        return "ynw-ms/assets/userManagement/components/user_info_management";
    }

    /**
     *  好友关系管理
     * @return
     */
    @RequestMapping("/components/friends_management")
    public String jumpLogManagement(String msResourceId, Model model) {
//        setModel(model, msResourceId,"assets/userManagement/components/friends_management",null);
        SetModel.setModel(msResourceId, model, "assets/userManagement/components/friends_management", null);
        return "ynw-ms/assets/userManagement/components/friends_management";
    }

    /**
     * 关注关系管理
     * @return
     */
    @RequestMapping("/components/attention_management")
    public String jumpAPPEditionManagement(String msResourceId, Model model) {
//        setModel(model, msResourceId,"assets/userManagement/components/attention_management",null);
        SetModel.setModel(msResourceId, model, "assets/userManagement/components/attention_management", null);
        return "ynw-ms/assets/userManagement/components/attention_management";
    }

    /**
     * 情豆管理
     * @return
     */
    @RequestMapping("/components/love_management")
    public String jumpReportManagement(String msResourceId, Model model) {
        SetModel.setModel(msResourceId, model, null, null);
        return "ynw-ms/assets/userManagement/components/love_management";
    }

    /**
     * 提现管理
     * @return
     */
    @RequestMapping("/components/withdraw_deposit")
    public String jumpWithdrawDeposit(String msResourceId, Model model) {
        SetModel.setModel(msResourceId, model, null, null);
        return "ynw-ms/assets/userManagement/components/withdraw_deposit";
    }

    /**
     * 糖果总量管理
     * @return
     */
    @RequestMapping("/components/withdraw_sum")
    public String jumpWithdrawSum(String parentId, String msResourceId, String name, String phone, String type, String beginDate,
                                  String endDate, String withdrawDeposit,Integer num, Model model) {
        SetModel.setModel(parentId, msResourceId, name, phone, type, beginDate, endDate, withdrawDeposit, num, model);
        return "ynw-ms/assets/userManagement/components/withdraw_sum";
    }

    /**
     * 邀请任务管理
     * @return
     */
    @RequestMapping("/components/invite_task")
    public String jumpInviteTask(String parentId, String msResourceId, String name, String phone, String type, String beginDate,
                                  String endDate, String withdrawDeposit,Integer num, Model model) {
        SetModel.setModel(parentId, msResourceId, name, phone, type, beginDate, endDate, withdrawDeposit, num, model);
        return "ynw-ms/assets/userManagement/components/invite_task";
    }

    /**
     *  签到管理
     * @return
     */
    @RequestMapping("/components/sign_in_management")
    public String jumpSensitiveManagement(String msResourceId, Model model) {
//        setModel(model, msResourceId,null,null);
        SetModel.setModel(msResourceId, model, null, null);
        return "ynw-ms/assets/userManagement/components/sign_in_management";
    }

    /**
     *  用户标签管理
     * @return
     */
    @RequestMapping("/components/user_label_management")
    public String jumpUserLabelManagement(String msResourceId, Model model, String name, String phone,String idVerifyStatus, String parentId,
                                          Integer no, Integer sex, String province, String city, Integer status, Integer imageStatus, Integer num) {
//        User user = UserController.userController.findUser();
//        if (null != user) {
//            model.addAttribute("msUserId", user.getMsUserId());
//            model.addAttribute("msResourceId", msResourceId);
//            model.addAttribute("name", name);
//            model.addAttribute("phone", phone);
//            model.addAttribute("no", no);
//            model.addAttribute("idVerifyStatus", idVerifyStatus);
//            model.addAttribute("sex", sex);
//            model.addAttribute("province", province);
//            model.addAttribute("city", city);
//            model.addAttribute("status", status);
//            model.addAttribute("imageStatus", imageStatus);
//            model.addAttribute("num", num);
//        }
        model.addAttribute("parentId", parentId);
        setModel(null, null, msResourceId, model, name, phone, idVerifyStatus, no, sex, province, city, status, imageStatus,num);
        return "ynw-ms/assets/userManagement/components/user_label_management";
    }

    /**
     *  用户标签管理
     * @return
     */
    @RequestMapping("/components/hierarchy_management")
    public String jumpHierarchyManagement(String msResourceId, Model model, String name, String phone,String idVerifyStatus,String parentId,
                                          Integer no, Integer sex, String province, String city, Integer status, Integer imageStatus, Integer num) {
        model.addAttribute("parentId", parentId);
        setModel(null, null, msResourceId, model, name, phone, idVerifyStatus, no, sex, province, city, status, imageStatus,num);
        return "ynw-ms/assets/userManagement/components/hierarchy_management";
    }

    /**
     *  用户详情
     * @return
     */
    @RequestMapping("/components/user_details_management")
    public String jumpUserDetailsManagement(String acUserId, String URL, String msResourceId, Model model, String name, String phone,String idVerifyStatus,
                                            Integer no, Integer sex, String province, String city, Integer status, Integer imageStatus, Integer num) {
//        model.addAttribute("acUserId", acUserId);
//        model.addAttribute("URL", URL);
//        model.addAttribute("msResourceId", msResourceId);
//        model.addAttribute("name", name);
//        model.addAttribute("phone", phone);
//        model.addAttribute("no", no);
//        model.addAttribute("idVerifyStatus", idVerifyStatus);
//        model.addAttribute("sex", sex);
//        model.addAttribute("province", province);
//        model.addAttribute("city", city);
//        model.addAttribute("status", status);
//        model.addAttribute("imageStatus", imageStatus);
//        model.addAttribute("num", num);
        setModel(acUserId, URL,msResourceId, model, name, phone, idVerifyStatus, no, sex, province, city, status, imageStatus, num);
        return "ynw-ms/assets/userManagement/components/user_details_management";
    }

    /**
     *  model赋值
     * @param model
     * @param msResourceId
     */
    public void setModel(String acUserId, String url, String msResourceId, Model model, String name, String phone,String idVerifyStatus,
                         Integer no, Integer sex, String province, String city, Integer status, Integer imageStatus, Integer num) {
        User user = UserController.userController.findUser();
        if (null != user) {
            model.addAttribute("msResourceId", msResourceId);
            model.addAttribute("msUserId", user.getMsUserId());
            if (null != acUserId) {
                model.addAttribute("acUserId", acUserId);
            }
            if (null != url) {
                model.addAttribute("URL", url);
            }
            model.addAttribute("msResourceId", msResourceId);
            model.addAttribute("name", name);
            model.addAttribute("phone", phone);
            model.addAttribute("no", no);
            model.addAttribute("idVerifyStatus", idVerifyStatus);
            model.addAttribute("sex", sex);
            model.addAttribute("province", province);
            model.addAttribute("city", city);
            model.addAttribute("status", status);
            model.addAttribute("imageStatus", imageStatus);
            model.addAttribute("num", num);
        }
    }

    /**
     *  model赋值
     * @param model
     * @param msResourceId
     */
//    public void setModel(Model model, String msResourceId, String url, String resourceName) {
//        User user = UserController.userController.findUser();
//        if (null != user) {
//            model.addAttribute("msResourceId", msResourceId);
//            model.addAttribute("msUserId", user.getMsUserId());
//            if (null != url) {
//                model.addAttribute("URL", url);
//            }
//            if (null != resourceName) {
//                model.addAttribute("resourceName", resourceName);
//            }
//        }
//    }

}
