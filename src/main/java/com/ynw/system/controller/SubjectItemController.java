package com.ynw.system.controller;

import com.ynw.system.entity.SubjectItem;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.SubjectItemService;
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
@RequestMapping("/ynw-ms/subjectItem")
@Api(tags = "问卷题目选项接口",description = "问卷题目选项查询及操作接口")
public class SubjectItemController {

    @Autowired
    private SubjectItemService subjectItemService;

    /**
     * 根据题目查询题目选项
     * @param subjectItem
     * @return
     */
//    @PostMapping("/conditionQuerySubjectItem")
//    public ResponseEntity<Result> conditionQuerySubjectItem(SubjectItem subjectItem) {
//        if (StringUtils.isNotEmpty(subjectItem.getKbSubjectId())) {
//            List<SubjectItem> subjectItemList = subjectItemService.conditionQuerySubjectItem(subjectItem);
//            if (subjectItemList.size() > 0) {
//                return ResultUtil.successResponse(subjectItemList);
//            }
//            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
//        }
//        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
//    }

    /**
     *  查询单个题目选项
     * @param subjectItem
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "查询单个题目选项", notes = "传入：题目选项id（kbSubjectItemId）")
    public ResponseEntity<Result> findById(SubjectItem subjectItem) {
        if (StringUtils.isNotEmpty(subjectItem.getKbSubjectItemId())) {
            SubjectItem item = subjectItemService.selectOne(subjectItem);
            if (null != item) {
                return ResultUtil.successResponse(item);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加题目选项
     * @param subjectItem
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加题目选项", notes = "传入：题目id（kbSubjectId），题目选项内容（content），" +
            "是否是路径的终结点（isPathEnd），问卷标准答案id（kbExampaperAnswerId），" +
            "下一题id（nextSubjectId），分值（score），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(SubjectItem subjectItem) {
        if (StringUtils.isNotEmpty(subjectItem.getContent()) && StringUtils.isNotEmpty(subjectItem.getKbSubjectId())
                && null != subjectItem.getIsPathEnd()) {
            SubjectItem item = subjectItemService.findBySortMax(subjectItem.getKbSubjectId());
            if (null != item) {
                if (null != item.getSort()) {
                    //数据库有数据并且最大的sort不为空
                    subjectItem.setSort(item.getSort() + 1);
                }
            }
            if (StringUtils.isEmpty(subjectItem.getKbExampaperAnswerId())) {
                subjectItem.setKbExampaperAnswerId(null);
            }
            if (subjectItemService.insert(subjectItem) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新题目选项
     * @param subjectItem
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新题目选项", notes = "传入：题目选项id(kbSubjectItemId),题目id（kbSubjectId），题目选项内容（content），" +
            "是否是路径的终结点（isPathEnd），问卷标准答案id（kbExampaperAnswerId），" +
            "下一题id（nextSubjectId），分值（score），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(SubjectItem subjectItem) {
        if (StringUtils.isNotEmpty(subjectItem.getContent()) && null != subjectItem.getIsPathEnd()
                && StringUtils.isNotEmpty(subjectItem.getKbSubjectItemId())) {
            SubjectItem item = subjectItemService.selectOne(new SubjectItem(subjectItem.getKbSubjectItemId()));
            if (null != item) {
                item.setContent(subjectItem.getContent());
                item.setIsPathEnd(subjectItem.getIsPathEnd());
                if (StringUtils.isNotEmpty(subjectItem.getNextSubjectId())) {
                    item.setNextSubjectId(subjectItem.getNextSubjectId());
                }
                if (StringUtils.isNotEmpty(subjectItem.getKbExampaperAnswerId())) {
                    item.setKbExampaperAnswerId(subjectItem.getKbExampaperAnswerId());
                }
                if (null != subjectItem.getScore()) {
                    item.setScore(subjectItem.getScore());
                }
                if (subjectItemService.update(item) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除题目选项
     * @param subjectItem
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除题目选项", notes = "传入：题目选项id(kbSubjectItemId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(SubjectItem subjectItem) {
        if (StringUtils.isNotEmpty(subjectItem.getKbSubjectItemId())) {
            if (subjectItemService.delete(subjectItem) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
