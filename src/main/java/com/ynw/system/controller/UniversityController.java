package com.ynw.system.controller;

import com.ynw.system.entity.Degrees;
import com.ynw.system.entity.University;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.DegreesService;
import com.ynw.system.service.UniversityService;
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
 *  @version 2018-12/8
 */
@RestController
@RequestMapping("/ynw-ms/university")
@Api(tags = "高校接口",description = "高校查询及操作接口")
public class UniversityController {

    @Autowired
    private UniversityService universityService;

    /**
     *  条件查询高校
     * @param university
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryUniversity")
    @ApiOperation(value = "条件查询高校", notes = "传入：页码（nowPage必传），高校名称（name），高校所在的城市id（bdCityId）,省份编号(provinceId)")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryUniversity(University university, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<University> universityList = universityService.conditionQueryUniversity(university, start, pageSize);
            if (universityList.size() > 0) {
                return ResultUtil.successResponse(universityList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询高校总数
     * @param university
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询高校总数", notes = "传入：高校名称（name），高校所在的城市id（bdCityId）,省份编号(provinceId)")
    public ResponseEntity<Result> count(University university) {
        return ResultUtil.successResponse(universityService.count(university));
    }

    /**
     *  根据编号查询高校
     * @param bdUniversityId 高校编号
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据编号查询高校", notes = "传入：高校编号（bdUniversityId)(必传)")
    @ApiImplicitParam(name = "bdUniversityId", value = "高校编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findById(String bdUniversityId) {
        if (StringUtils.isNotEmpty(bdUniversityId)) {
            University university = universityService.findById(bdUniversityId);
            if (null != university) {
                return ResultUtil.successResponse(university);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加高校
     * @param university
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加高校", notes = "传入：高校名称（name），高校所在的城市id（bdCityId）,省份编号(provinceId)," +
            "添加系统日志的主体(LogContent)")
    public ResponseEntity<Result> insert(University university) {
        if (StringUtils.isNotEmpty(university.getName()) && StringUtils.isNotEmpty(university.getBdCityId())) {
            if (universityService.insert(university) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新高校
     * @param university
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新高校", notes = "传入：高校id(bdUniversityId),高校名称（name），高校所在的城市id（bdCityId）,省份编号(provinceId)," +
            "添加系统日志的主体(LogContent)")
    public ResponseEntity<Result> update(University university) {
        if (StringUtils.isNotEmpty(university.getName()) && StringUtils.isNotEmpty(university.getBdCityId())
                && StringUtils.isNotEmpty(university.getBdUniversityId())) {
            University university1 = universityService.selectOne(new University(university.getBdUniversityId()));
            if (null != university1) {
                university1.setName(university.getName());
                university1.setBdCityId(university.getBdCityId());
                if (universityService.update(university1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除高校
     * @param university
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除高校", notes = "传入：高校id(bdUniversityId),添加系统日志的主体(LogContent)")
    public ResponseEntity<Result> delete(University university) {
        if (StringUtils.isNotEmpty(university.getBdUniversityId())) {
            if (universityService.delete(university) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
