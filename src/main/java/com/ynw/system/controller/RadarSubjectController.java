package com.ynw.system.controller;

import com.ynw.system.entity.RadarSubject;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.RadarSubjectService;
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
@RequestMapping("/ynw-ms/radarSubject")
@Api(tags = "雷达搜题目接口",description = "雷达搜题目查询及操作接口")
public class RadarSubjectController {

    @Autowired
    private RadarSubjectService radarSubjectService;

    /**
     *  根据条件分页查询数据
     * @param subject
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryRadarSubject")
    @ApiOperation(value = "根据条件分页查询数据", notes = "传入：页码（nowPage必传），出题人（userName），题目类型（type），" +
            "题目出处（fromType），适用范围（applied）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryRadarSubject(RadarSubject subject, Integer nowPage) {
        if (null == nowPage || nowPage == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<RadarSubject> radarSubjectList = radarSubjectService.conditionQueryRadarSubject(subject, pageSize, start);
        if (radarSubjectList.size() > 0)
            return ResultUtil.successResponse(radarSubjectList);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据条件统计数据总数
     * @param subject
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "根据条件统计数据总数", notes = "传入：出题人（userName），题目类型（type），" +
            "题目出处（fromType），适用范围（applied）")
    public ResponseEntity<Result> count(RadarSubject subject) {
        return ResultUtil.successResponse(radarSubjectService.cpunt(subject));
    }

    /**
     *  根据条件查询单条数据
     * @param radarSubject
     * @return
     */
    @PostMapping("/selectOne")
    @ApiOperation(value = "根据条件查询单条数据", notes = "传入：雷达搜用户题目id（rsSubjectId），题目内容（content），题目类型（type），" +
            "题目出处（fromType），自定义题目的用户id（acUserId），适用范围（applied）")
    public ResponseEntity<Result> selectOne(RadarSubject radarSubject) {
        RadarSubject subject = radarSubjectService.selectOne(radarSubject);
        if (null != subject)
            return ResultUtil.successResponse(subject);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加系统题目
     * @param subject
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加系统题目", notes = "传入：题目内容（content），题目类型（type），" +
            "题目出处（fromType），自定义题目的用户id（acUserId），适用范围（applied），日志主体（LogContent）")
    public ResponseEntity<Result> insert(RadarSubject subject) {
        if (StringUtils.isEmpty(subject.getContent()) || StringUtils.isEmpty(subject.getType()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        subject.setFromType(0);
        List<RadarSubject> radarSubjectList = radarSubjectService.findBySortMax(subject);
        if (radarSubjectList.size() > 0) {
            subject.setSort(radarSubjectList.get(0).getSort() + 1);
        }
        if (radarSubjectService.insert(subject) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  修改系统题目
     * @param subject
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "添加系统题目", notes = "传入：雷达搜用户题目id（rsSubjectId），题目内容（content），题目类型（type），" +
            "题目出处（fromType），自定义题目的用户id（acUserId），适用范围（applied），日志主体（LogContent）")
    public ResponseEntity<Result> update(RadarSubject subject) {
        if (StringUtils.isEmpty(subject.getContent()) || StringUtils.isEmpty(subject.getType()) || StringUtils.isEmpty(subject.getRsSubjectId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        RadarSubject radarSubject = radarSubjectService.selectOne(new RadarSubject(subject.getRsSubjectId()));
        if (null == radarSubject)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        if (radarSubject.getFromType() == 1)
            return ResultUtil.errorResponse(ResultEnums.USER_QUESTIONS_CANNOT_BE_MODIFIED);
        radarSubject.setContent(subject.getContent());
        radarSubject.setType(subject.getType());
        radarSubject.setApplied(subject.getApplied());
        radarSubject.setFromType(subject.getFromType());
        radarSubject.setAcUserId(subject.getAcUserId());
        if (radarSubjectService.update(radarSubject) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  删除题库
     * @param subject
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除题库", notes = "传入：雷达搜用户题目id（rsSubjectId），日志主体（LogContent）")
    public ResponseEntity<Result> delete(RadarSubject subject) {
        if (StringUtils.isEmpty(subject.getRsSubjectId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (radarSubjectService.delete(subject) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

}
