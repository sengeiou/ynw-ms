package com.ynw.system.controller;

import com.ynw.system.entity.RadarSubjectItem;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.RadarSubjectItemService;
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

@RestController
@RequestMapping("/ynw-ms/radarSubjectItem")
@Api(tags = "雷达搜题目选项接口",description = "雷达搜题目选项查询及操作接口")
public class RadarSubjectItemController {

    @Autowired
    private RadarSubjectItemService radarSubjectItemService;

    /**
     *  根据题目编号查询所有选项
     * @param subjectId
     * @return
     */
    @PostMapping("/findAllBySubjectId")
    @ApiOperation(value = "根据题目编号查询所有选项", notes = "传入：雷达搜题目（subjectId）")
    @ApiImplicitParam(value = "subjectId", name = "雷达搜题目", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findAllBySubjectId(String subjectId) {
        if (StringUtils.isEmpty(subjectId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        List<RadarSubjectItem> radarSubjectItems = radarSubjectItemService.findAllBySubjectId(subjectId);
        if (radarSubjectItems.size() > 0)
            return ResultUtil.successResponse(radarSubjectItems);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加系统题目选项
     * @param item
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加系统题目选项", notes = "传入：题目选项内容（content），雷达搜用户题目id（rsSubjectId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(RadarSubjectItem item) {
        if (StringUtils.isEmpty(item.getContent()) || StringUtils.isEmpty(item.getRsSubjectId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        List<RadarSubjectItem> radarSubjectItemList = radarSubjectItemService.findBySortMax(item.getRsSubjectId());
        if (radarSubjectItemList.size() > 0) {
            item.setSort(radarSubjectItemList.get(0).getSort() + 1);
        }
        if (radarSubjectItemService.insert(item) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  修改系统题目选项
     * @param item
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改系统题目选项", notes = "传入：雷达搜用户题目选项id(rsSubjectItemId),题目选项内容（content），雷达搜用户题目id（rsSubjectId），" +
            "添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(RadarSubjectItem item) {
        if (StringUtils.isEmpty(item.getContent()) || StringUtils.isEmpty(item.getRsSubjectItemId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        RadarSubjectItem subjectItem = radarSubjectItemService.selectOne(new RadarSubjectItem(item.getRsSubjectItemId()));
        if (null == subjectItem)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        subjectItem.setContent(item.getContent());
        if (radarSubjectItemService.update(subjectItem) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  删除题库选项
     * @param item
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除题库选项", notes = "传入：雷达搜用户题目选项id(rsSubjectItemId)," +
            "添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(RadarSubjectItem item) {
        if (StringUtils.isEmpty(item.getRsSubjectItemId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (radarSubjectItemService.delete(item) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

}
