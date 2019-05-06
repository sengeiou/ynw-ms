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
@RequestMapping("ynw-ms/assets/systemManagement")
public class JumpSystemManagement {

    @RequestMapping("/index")
    public String jumpLogin(String name, String msResourceId, Model model) {
        model.addAttribute("resourceName",name);
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/index";
    }

    /**
     *  undefined
     * @return
     */
    @RequestMapping("/undefined")
    public String jumpUndefined() {
        return "ynw-ms/assets/systemManagement/undefined";
    }

    /**
     *  系统参数配置
     * @return
     */
    @RequestMapping("/components/parameter_management")
    public String jumpParameterManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/components/parameter_management";
    }

    /**
     *  操作日志管理
     * @return
     */
    @RequestMapping("/components/log_management")
    public String jumpLogManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/components/log_management";
    }

    /**
     * APP版本管理
     * @return
     */
    @RequestMapping("/components/APP_edition_management")
    public String jumpAPPEditionManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/components/APP_edition_management";
    }

    /**
     * 举报信息管理
     * @return
     */
    @RequestMapping("/components/report_management")
    public String jumpReportManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/components/report_management";
    }

    /**
     *  举报话题详情
     * @return
     */
    @RequestMapping("/components/mood_details_management")
    public String jumpMoodDetailsManagement(String targetId, String msResourceId, Model model, String name, String phone,
                                            String syReportCtgyId, String targetType, Integer status, Integer num) {
        SetModel.setModel(targetId, msResourceId, model, name, phone, syReportCtgyId, targetType, status, num);
        return "ynw-ms/assets/systemManagement/components/mood_details_management";
    }

    /**
     *  举报对象统计跳转
     * @param msResourceId
     * @param model
     * @return
     */
    @RequestMapping("/components/report_mood_management")
    public String jumpReportMoodManagement(String msResourceId, Model model, String name, String phone,
                                           String syReportCtgyId, String targetType, Integer status, Integer num) {
        SetModel.setModel(null, msResourceId, model, name, phone, syReportCtgyId, targetType, status, num);
        return "ynw-ms/assets/systemManagement/components/report_mood_management";
    }

    /**
     *  敏感词管理
     * @return
     */
    @RequestMapping("/components/word_filter_management")
    public String jumpSensitiveManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/components/word_filter_management";
    }

    /**
     *  字典管理
     * @return
     */
    @RequestMapping("/components/dictionary_management")
    public String jumpDictionaryManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/components/dictionary_management";
    }

    /**
     *  推送管理
     * @return
     */
    @RequestMapping("/components/push_management")
    public String jumpPushManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/components/push_management";
    }

    /**
     *  用户反馈管理
     * @return
     */
    @RequestMapping("/components/advice_management")
    public String jumpAdviceManagement(String msResourceId, Model model, String parentId) {
        model.addAttribute("parentId", parentId);
        SetModel.setModel(model, msResourceId,null, null);
        return "ynw-ms/assets/systemManagement/components/advice_management";
    }

}
