package com.ynw.system.controller;

import com.ynw.system.entity.ExamPaperAnswer;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ExamPaperAnswerService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/10
 */
@RestController
@RequestMapping("/ynw-ms/examPaperAnswer")
@Api(tags = "问卷答案接口",description = "问卷答案查询及操作接口")
public class ExamPaperAnswerController {

    @Autowired
    private ExamPaperAnswerService examPaperAnswerService;

    /**
     * 根据问卷查询答案
     * @param examPaperAnswer
     * @return
     */
    @PostMapping("/conditionQueryExamPaperAnswer")
    @ApiOperation(value = "根据问卷查询答案", notes = "传入：问卷id（kbExampaperId）")
    public ResponseEntity<Result> conditionQueryExamPaperAnswer(ExamPaperAnswer examPaperAnswer) {
        if (StringUtils.isNotEmpty(examPaperAnswer.getKbExampaperId())) {
            List<ExamPaperAnswer> examPaperAnswerList = examPaperAnswerService.conditionQueryExamPaperAnswer(examPaperAnswer);
            if (examPaperAnswerList.size() > 0) {
                return ResultUtil.successResponse(examPaperAnswerList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加答案
     * @param examPaperAnswer
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加答案", notes = "传入：问卷id（kbExampaperId），题目答案内容（content），" +
            "最小分值条件（minScore），最大分值条件（maxScore），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(ExamPaperAnswer examPaperAnswer) {
        if (StringUtils.isNotEmpty(examPaperAnswer.getContent()) && StringUtils.isNotEmpty(examPaperAnswer.getKbExampaperId())) {
           ExamPaperAnswer answer = examPaperAnswerService.findBySortMax(examPaperAnswer);
           if (null != answer) {
               if (null != answer.getSort()) {
                   examPaperAnswer.setSort(answer.getSort() + 1);
               }
           }
            if (examPaperAnswerService.insert(examPaperAnswer) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新答案
     * @param examPaperAnswer
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新答案", notes = "传入：问卷标准答案id（kbExampaperAnswerId），问卷id（kbExampaperId），题目答案内容（content），" +
            "最小分值条件（minScore），最大分值条件（maxScore），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(ExamPaperAnswer examPaperAnswer) {
        if (StringUtils.isNotEmpty(examPaperAnswer.getKbExampaperAnswerId()) && StringUtils.isNotEmpty(examPaperAnswer.getContent())) {
            ExamPaperAnswer answer = examPaperAnswerService.selectOne(new ExamPaperAnswer(examPaperAnswer.getKbExampaperAnswerId()));
            if (null != answer) {
                answer.setContent(examPaperAnswer.getContent());
                answer.setMaxScore(examPaperAnswer.getMaxScore());
                answer.setMinScore(examPaperAnswer.getMinScore());
                if (examPaperAnswerService.update(answer) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除答案
     * @param examPaperAnswer
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "更新答案", notes = "传入：问卷标准答案id（kbExampaperAnswerId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(ExamPaperAnswer examPaperAnswer) {
        if (StringUtils.isNotEmpty(examPaperAnswer.getKbExampaperAnswerId())) {
            if (examPaperAnswerService.delete(examPaperAnswer) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
