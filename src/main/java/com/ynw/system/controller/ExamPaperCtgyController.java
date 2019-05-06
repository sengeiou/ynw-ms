package com.ynw.system.controller;

import com.ynw.system.entity.ExamPaperCtgy;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ExamPaperCtgyService;
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
@RequestMapping("/ynw-ms/examPaperCtgy")
@Api(tags = "问卷类型接口",description = "问卷类型查询及操作接口")
public class ExamPaperCtgyController {

    @Autowired
    private ExamPaperCtgyService examPaperCtgyService;


    /**
     *  查询所有问卷类型
     * @return
     */
    @PostMapping("/findExamPaperCtgyAll")
    @ApiOperation(value = "查询所有问卷类型")
    public ResponseEntity<Result> findExamPaperCtgyAll() {
        List<ExamPaperCtgy> examPaperCtgieList = examPaperCtgyService.findAll();
        if (examPaperCtgieList.size() > 0) {
            return ResultUtil.successResponse(examPaperCtgieList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加问卷类型
     * @param examPaperCtgy
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加问卷类型", notes = "传入：分类名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(ExamPaperCtgy examPaperCtgy) {
        if (StringUtils.isNotEmpty(examPaperCtgy.getName())) {
            List<ExamPaperCtgy> examPaperCtgyList = examPaperCtgyService.findBySortMax();
            if (examPaperCtgyList.size() > 0) {
                if (null != examPaperCtgyList.get(0).getSort()) {
                    //数据库有数据并且最大的sort不为空
                    examPaperCtgy.setSort(examPaperCtgyList.get(0).getSort() + 1);
                }
            }
            if (examPaperCtgyService.insert(examPaperCtgy) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新问卷类型
     * @param examPaperCtgy
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新问卷类型", notes = "传入：问卷分类id（kbExampaperCtgyId），分类名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(ExamPaperCtgy examPaperCtgy) {
        if (StringUtils.isNotEmpty(examPaperCtgy.getName()) && StringUtils.isNotEmpty(examPaperCtgy.getKbExampaperCtgyId())) {
            ExamPaperCtgy ctgy = examPaperCtgyService.selectOne(new ExamPaperCtgy(examPaperCtgy.getKbExampaperCtgyId()));
            if (null != ctgy) {
                ctgy.setName(examPaperCtgy.getName());
                if (examPaperCtgyService.update(ctgy) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除问卷类型
     * @param examPaperCtgy
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除问卷类型", notes = "传入：问卷分类id（kbExampaperCtgyId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(ExamPaperCtgy examPaperCtgy) {
        if (StringUtils.isNotEmpty(examPaperCtgy.getKbExampaperCtgyId())) {
            if (examPaperCtgyService.delete(examPaperCtgy) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param kbExampaperCtgyId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（kbExampaperCtgyId)(必传)")
    @ApiImplicitParam(name = "kbExampaperCtgyId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String kbExampaperCtgyId) {
        return move(kbExampaperCtgyId, 1);
    }

    /**
     *  下移
     * @param kbExampaperCtgyId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（kbExampaperCtgyId)(必传)")
    @ApiImplicitParam(name = "kbExampaperCtgyId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String kbExampaperCtgyId) {
        return move(kbExampaperCtgyId, -1);
    }

    /**
     *  上移下移复用
     * @param kbExampaperCtgyId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String kbExampaperCtgyId, Integer move) {
        if (StringUtils.isNotEmpty(kbExampaperCtgyId)) {
            List<ExamPaperCtgy> examPaperCtgyList = examPaperCtgyService.findBySortMax();
            if (examPaperCtgyList.size() > 0) {
                if (null == examPaperCtgyList.get(0).getSort()) {
                    return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
                }
                Integer code = examPaperCtgyService.move(kbExampaperCtgyId, examPaperCtgyList, move);
                if (code > 0) {
                    if (code == 14) {
                        return ResultUtil.errorResponse(ResultEnums.MOVE_UP_ERROR);
                    } else if (code == 15) {
                        return ResultUtil.errorResponse(ResultEnums.MOVE_DOWN_ERROR);
                    } else {
                        return ResultUtil.successResponse();
                    }
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
