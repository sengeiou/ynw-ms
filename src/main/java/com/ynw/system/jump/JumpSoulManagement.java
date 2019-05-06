package com.ynw.system.jump;

import com.mysql.cj.jdbc.util.ResultSetUtil;
import com.ynw.system.controller.UserController;
import com.ynw.system.entity.User;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import com.ynw.system.util.SetModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 灵魂管理
 *  @author ChengZhi
 *  @version 2018-11-27
 */
@Controller
@RequestMapping("ynw-ms/assets/soulManagement")
public class JumpSoulManagement {

    /**
     *  权限管理主页
     * @return
     */
    @RequestMapping("/index")
    public String jumpIndex(String name, String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, name);
        return "ynw-ms/assets/soulManagement/index";
    }

    /**
     *  undefined
     * @return
     */
    @RequestMapping("/undefined")
    public String jumpUndefined() {
        return "ynw-ms/assets/soulManagement/undefined";
    }

    /**
     *  题库管理
     * @return
     */
    @RequestMapping("/components/exam_paper_management")
    public String jumpExamPaperManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, "assets/soulManagement/components/exam_paper_management", null);
        return "ynw-ms/assets/soulManagement/components/exam_paper_management";
    }

    /**
     *  用户测试管理
     * @return
     */
    @RequestMapping("/components/exam_paper_test_management")
    public String jumpExamPaperTestManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, "assets/soulManagement/components/exam_paper_test_management", null);
        return "ynw-ms/assets/soulManagement/components/exam_paper_test_management";
    }

    /**
     * 话题信息管理
     * @return
     */
    @RequestMapping("/components/mood_management")
    public String jumpMoodManagement(String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, "assets/soulManagement/components/mood_management", null);
        return "ynw-ms/assets/soulManagement/components/mood_management";
    }

    /**
     *  编辑题目
     * @return
     */
    @RequestMapping("/components/edit_the_title_management")
    public String jumpEditTheTitleManagement(String type, String kbExamPaperId, String URL, String msResourceId, Model model) {
        model.addAttribute("kbExamPaperId", kbExamPaperId);
        model.addAttribute("URL", URL);
        model.addAttribute("msResourceId", msResourceId);
        model.addAttribute("type", type);
        return "ynw-ms/assets/soulManagement/components/edit_the_title_management";
    }

    /**
     *  测试详情
     * @return
     */
    @RequestMapping("/components/test_details_management")
    public String jumpTestDetailsManagement(String kbExampaperTestId, String URL, String msResourceId, Model model) {
        model.addAttribute("kbExampaperTestId", kbExampaperTestId);
        model.addAttribute("URL", URL);
        model.addAttribute("msResourceId", msResourceId);
        return "ynw-ms/assets/soulManagement/components/test_details_management";
    }

    /**
     *  话题详情
     * @return
     */
    @RequestMapping("/components/mood_details_management")
    public String jumpMoodDetailsManagement(String moodId, String URL, String msResourceId, Model model) {
        SetModel.setModel(model, msResourceId, URL, moodId);
        return "ynw-ms/assets/soulManagement/components/mood_details_management";
    }

}
