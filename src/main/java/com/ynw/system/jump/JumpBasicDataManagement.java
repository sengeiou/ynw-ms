package com.ynw.system.jump;

import com.ynw.system.controller.UserController;
import com.ynw.system.entity.User;
import com.ynw.system.util.SetModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 基础数据
 *  @author ChengZhi
 *  @version 2018-11-27
 */
@Controller
@RequestMapping("ynw-ms/assets/basicDataManagement")
public class JumpBasicDataManagement {

    @RequestMapping("/index")
    public String jumpBasicDataManagement(String name, String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, name);
        return "ynw-ms/assets/basicDataManagement/index";
    }

    /**
     *  undefined
     * @return
     */
    @RequestMapping("/undefined")
    public String jumpUndefined() {
        return "ynw-ms/assets/basicDataManagement/undefined";
    }

    /**
     *  行政区划管理
     * @return
     */
    @RequestMapping("/components/administrative_management")
    public String jumpAdministrativeManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/basicDataManagement/components/administrative_management";
    }

    /**
     *  行业管理
     * @return
     */
    @RequestMapping("/components/business_management")
    public String jumpBusinessManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/basicDataManagement/components/business_management";
    }

    /**
     * 学历管理
     * @return
     */
    @RequestMapping("/components/education_management")
    public String jumpEducationManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/basicDataManagement/components/education_management";
    }

    /**
     * 高校管理
     * @return
     */
    @RequestMapping("/components/university_management")
    public String jumpUniversityManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/basicDataManagement/components/university_management";
    }

    /**
     *  兴趣标签管理
     * @return
     */
    @RequestMapping("/components/interest_management")
    public String jumpInterestManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/basicDataManagement/components/interest_management";
    }

    /**
     *  自我标签管理
     * @return
     */
    @RequestMapping("/components/self_label_management")
    public String jumpSelfLabelManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/basicDataManagement/components/self_label_management";
    }

}
