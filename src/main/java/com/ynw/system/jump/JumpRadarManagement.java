package com.ynw.system.jump;

import com.ynw.system.controller.UserController;
import com.ynw.system.entity.User;
import com.ynw.system.util.SetModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 灵魂管理
 *  @author ChengZhi
 *  @version 2018-11-27
 */
@Controller
@RequestMapping("ynw-ms/assets/radarManagement")
public class JumpRadarManagement {

    /**
     *  雷达管理主页
     * @return
     */
    @RequestMapping("/index")
    public String jumpIndex(String name, String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, name);
        return "ynw-ms/assets/radarManagement/index";
    }

    /**
     *  undefined
     * @return
     */
    @RequestMapping("/undefined")
    public String jumpUndefined() {
        return "ynw-ms/assets/radarManagement/undefined";
    }

    /**
     *  雷达搜题库管理
     * @return
     */
    @RequestMapping("/components/radar_subject_management")
    public String jumpRadarSubjectManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/radarManagement/components/radar_subject_management";
    }

    /**
     *  雷达搜频道管理
     * @return
     */
    @RequestMapping("/components/channel_management")
        public String jumpChannelManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/radarManagement/components/channel_management";
    }

    /**
     *  雷达搜短语管理
     * @return
     */
    @RequestMapping("/components/phrase_management")
    public String jumpPhraseManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, null);
        return "ynw-ms/assets/radarManagement/components/phrase_management";
    }

}
