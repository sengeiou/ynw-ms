package com.ynw.system.controller;

import com.ynw.system.entity.Mood;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.MoodService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/14
 */
@RestController
@RequestMapping("/ynw-ms/mood")
@Api(tags = "用户话题接口",description = "用户话题查询及操作接口")
public class MoodController {

    @Autowired
    private MoodService moodService;

    /**
     *  条件查询话题
     * @param mood
     * @param nowPage 当前页
     * @param label 标签编号
     * @return
     */
    @PostMapping("/conditionQueryMood")
    @ApiOperation(value = "条件查询话题", notes = "传入：页码（nowPage必传），用户手机（userPhone）,标签编号(label)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1") ,
            @ApiImplicitParam(name = "label", value = "标签编号", dataType = "String", paramType = "query") ,
    })

    public ResponseEntity<Result> conditionQueryMood(Mood mood, Integer nowPage, String label) {
        if (null != nowPage && nowPage > 0) {
            if (StringUtils.isEmpty(label)) {
                label = null;
            }
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Mood> moodList = moodService.conditionQueryMood(mood, label, start, pageSize);
            if (moodList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                if (StringUtils.isNotEmpty(url)) {
                    for (Mood mood1 :moodList
                    ) {
                        //统计数为空赋值为0
                        mood1.assignment();
                        //完整路径
                        mood1.setUserImage(url+mood1.getUserImage());
                    }
                }
                return ResultUtil.successResponse(moodList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总数
     * @param mood
     * @param label 标签编号
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总数", notes = "传入：用户手机（userPhone）,标签编号(label)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "label", value = "标签编号", dataType = "String", paramType = "query") ,
    })
    public ResponseEntity<Result> count(Mood mood, String label) {
        if (StringUtils.isEmpty(label)) {
            label = null;
        }
        return ResultUtil.successResponse(moodService.count(mood, label));
    }

    /**
     *  根据编号查询单个数据
     * @param dsMoodId 编号
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据编号查询单个数据", notes = "传入：编号（dsMoodId）")
    @ApiImplicitParam(name = "dsMoodId", value = "标签编号",required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findById(String dsMoodId) {
        if (StringUtils.isEmpty(dsMoodId)) {
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        Mood mood = moodService.findById(dsMoodId);
        if (null != mood) {
            String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
            if (StringUtils.isNotEmpty(url)) {
                //用户头像路径完整路径
                mood.setUserImage(url+mood.getUserImage());
                List<String> moodImg = new ArrayList<>();
                for (String string: mood.getMoodImgList()
                ) {
                    moodImg.add(url + string);
                }
                mood.setMoodImgList(moodImg);
            }
            return ResultUtil.successResponse(mood);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据根目录查询评论和转发
     * @param rootMoodId 根目录编号
     * @return
     */
    @PostMapping("/findMoodCommentByRootMoodId")
    @ApiOperation(value = "根据根目录查询评论和转发", notes = "传入：根目录编号（rootMoodId）")
    @ApiImplicitParam(name = "rootMoodId", value = "根目录编号",required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findMoodCommentByRootMoodId(String rootMoodId) {
        if (StringUtils.isEmpty(rootMoodId)) {
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        List<Mood> moodList = moodService.findMoodCommentByRootMoodId(rootMoodId);
        if (moodList.size() > 0) {
            String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
            if (StringUtils.isNotEmpty(url)) {
                for (Mood mood :moodList
                ) {
                    //用户头像路径完整路径
                    mood.setUserImage(url+mood.getUserImage());
                }
            }
            return ResultUtil.successResponse(moodList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  逻辑删除
     * @param mood
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "逻辑删除", notes = "传入：编号（dsMoodId），推送删除理由（content）")
    @ApiImplicitParam(name = "content", value = "推送内容",dataType = "String", paramType = "query")
    public ResponseEntity<Result> delete(Mood mood, String content) {
        if (StringUtils.isNotEmpty(mood.getDsMoodId())) {
            if (moodService.delete(mood.getDsMoodId(), content) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
