package com.ynw.system.controller;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.ynw.system.entity.Subject;
import com.ynw.system.entity.SubjectItem;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.SubjectItemService;
import com.ynw.system.service.SubjectService;
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

import java.util.ArrayList;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/10
 */
@RestController
@RequestMapping("/ynw-ms/subject")
@Api(tags = "问卷题目接口",description = "问卷题目查询及操作接口")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SubjectItemService subjectItemService;

    /**
     * 根据问卷查询题目
     * @param subject
     * @return
     */
    @PostMapping("/conditionQuerySubject")
    @ApiOperation(value = "根据问卷查询题目", notes = "传入：问卷id（kbExampaperId）")
    public ResponseEntity<Result> conditionQuerySubject(Subject subject) {
        if (StringUtils.isNotEmpty(subject.getKbExampaperId())) {
            List<Subject> subjectList = subjectService.conditionQuerySubject(subject);
            if (subjectList.size() > 0) {
                return ResultUtil.successResponse(subjectList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     * 获取没有选择的题目
     * @param subject
     * @return
     */
    @PostMapping("/findByNextSubjectId")
    @ApiOperation(value = "获取没有选择的题目", notes = "传入：问卷id（kbExampaperId）,排序(sort)")
    public ResponseEntity<Result> findByNextSubjectId(Subject subject) {
        if (StringUtils.isNotEmpty(subject.getKbExampaperId())) {
            List<Subject> subjectList = subjectService.findByExamIdAndSort(subject);
            if (subjectList.size() > 0) {
                return ResultUtil.successResponse(subjectList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     * 根据编号查询题目
     * @param subject
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "获取没有选择的题目", notes = "传入：题目id（kbSubjectId）")
    public ResponseEntity<Result> findById(Subject subject) {
        if (StringUtils.isNotEmpty(subject.getKbSubjectId())) {
            Subject subject1 = subjectService.selectOne(subject);
            if (null != subject1) {
                return ResultUtil.successResponse(subject1);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加题目
     * @param subject
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加题目", notes = "传入：题目内容（content），题目类型（type），题目的路径类型（pathType），" +
            "添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Subject subject) {
        if (StringUtils.isNotEmpty(subject.getContent()) && StringUtils.isNotEmpty(subject.getKbExampaperId())
                && StringUtils.isNotEmpty(subject.getPathType()) && StringUtils.isNotEmpty(subject.getType())) {
            Subject subject1 = subjectService.findBySortMax(subject);
            if (null != subject1) {
                if (null != subject1.getSort()) {
                    //数据库有数据并且最大的sort不为空
                    subject.setSort(subject1.getSort() + 1);
                }
            }
            if (subjectService.insert(subject) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新题目
     * @param subject
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新题目", notes = "传入：题目id(kbSubjectId),题目内容（content），题目类型（type），题目的路径类型（pathType），" +
            "添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Subject subject) {
        if (StringUtils.isNotEmpty(subject.getKbSubjectId()) && StringUtils.isNotEmpty(subject.getType())
                && StringUtils.isNotEmpty(subject.getPathType()) && StringUtils.isNotEmpty(subject.getContent())) {
            Subject subject1 = subjectService.selectOne(new Subject(subject.getKbSubjectId()));
            if (null != subject1) {
                subject1.setContent(subject.getContent());
                subject1.setPathType(subject.getPathType());
                subject1.setType(subject.getType());
                if (subjectService.update(subject1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除题目
     * @param subject
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "更新题目", notes = "传入：题目id(kbSubjectId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Subject subject) {
        if (StringUtils.isNotEmpty(subject.getKbSubjectId())) {
            if (subjectService.deleteSubject(subject) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
