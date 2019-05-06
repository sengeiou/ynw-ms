package com.ynw.system.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.ynw.system.entity.ExamPaper;
import com.ynw.system.entity.ExamPaperCtgy;
import com.ynw.system.entity.Report;
import com.ynw.system.entity.ReportType;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ExamPaperCtgyService;
import com.ynw.system.service.ExamPaperService;
import com.ynw.system.service.ReportService;
import com.ynw.system.service.ReportTypeService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
 *  @version 2018-12/7
 */
@RestController
@RequestMapping("/ynw-ms/examPaper")
@Api(tags = "问卷接口",description = "问卷查询及操作接口")
public class ExamPaperController {

    @Autowired
    private ExamPaperService examPaperService;

    /**
     *  条件查询问卷
     * @param examPaper
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryExamPaper")
    @ApiOperation(value = "条件查询问卷", notes = "传入：页码（nowPage必传），问卷标题（title），问卷分类id（kbExampaperCtgyId），" +
            "问卷类型（type）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryExamPaper(ExamPaper examPaper, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<ExamPaper> examPaperList = examPaperService.conditionQueryExamPaper(examPaper, start, pageSize);
            if (examPaperList.size() > 0) {
                return ResultUtil.successResponse(examPaperList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总数
     * @param examPaper
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总数", notes = "传入：问卷标题（title），问卷分类id（kbExampaperCtgyId），" +
            "问卷类型（type）")
    public ResponseEntity<Result> count(ExamPaper examPaper) {
        return ResultUtil.successResponse(examPaperService.count(examPaper));
    }

    /**
     *  获取单个数据
     * @param examPaper
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "获取单个数据", notes = "传入：问卷id（kbExampaperId）")
    public ResponseEntity<Result> findById(ExamPaper examPaper) {
        if (StringUtils.isNotEmpty(examPaper.getKbExampaperId())) {
            ExamPaper paper = examPaperService.selectOne(examPaper);
            if (null != paper) {
                return ResultUtil.successResponse(paper);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加问卷
     * @param examPaper
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加问卷", notes = "传入：问卷分类id（kbExampaperCtgyId），问卷标题（title），" +
            "问卷类型（type），状态（status），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(ExamPaper examPaper) {
        if (StringUtils.isNotEmpty(examPaper.getTitle()) && StringUtils.isNotEmpty(examPaper.getKbExampaperCtgyId())
                && StringUtils.isNotEmpty(examPaper.getType())) {
            ExamPaper paper = examPaperService.findBySortMax();
            if (null != paper) {
                if (null != paper.getSort()) {
                    //数据库有数据并且最大的sort不为空
                    examPaper.setSort(paper.getSort() + 1);
                }
            }
            if (examPaperService.insert(examPaper) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新问卷
     * @param examPaper
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新问卷", notes = "传入：问卷id（kbExampaperId），问卷分类id（kbExampaperCtgyId），问卷标题（title），" +
            "问卷类型（type），状态（status），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(ExamPaper examPaper) {
        if (StringUtils.isNotEmpty(examPaper.getTitle()) && StringUtils.isNotEmpty(examPaper.getKbExampaperCtgyId())
                && StringUtils.isNotEmpty(examPaper.getType()) && StringUtils.isNotEmpty(examPaper.getKbExampaperId())) {
            ExamPaper paper = examPaperService.selectOne(new ExamPaper(examPaper.getKbExampaperId()));
            if (null != paper) {
                paper.setTitle(examPaper.getTitle());
                paper.setKbExampaperCtgyId(examPaper.getKbExampaperCtgyId());
                paper.setType(examPaper.getType());
                paper.setStatus(examPaper.getStatus());
                if (examPaperService.update(paper) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除问卷
     * @param examPaper
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除问卷", notes = "传入：问卷id（kbExampaperId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(ExamPaper examPaper) {
        if (StringUtils.isNotEmpty(examPaper.getKbExampaperId())) {
            if (examPaperService.delete(examPaper) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改问卷状态
     * @param examPaper
     * @return
     */
    @PostMapping("/updateStatus")
    @ApiOperation(value = "更新问卷", notes = "传入：问卷id（kbExampaperId），状态（status），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> updateStatus(ExamPaper examPaper) {
        if (StringUtils.isNotEmpty(examPaper.getKbExampaperId())) {
            ExamPaper paper = examPaperService.selectOne(examPaper);
            if (null != paper) {
                if (paper.getStatus() == 1) {
                    paper.setStatus(0);
                } else {
                    paper.setStatus(1);
                }
                if (examPaperService.update(paper) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
