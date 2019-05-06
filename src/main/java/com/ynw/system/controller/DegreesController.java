package com.ynw.system.controller;

import com.ynw.system.entity.Degrees;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.DegreesService;
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
@RequestMapping("/ynw-ms/degrees")
@Api(tags = "学历接口",description = "学历查询及操作接口")
public class DegreesController {

    @Autowired
    private DegreesService degreesService;

    /**
     *  条件查询学历
     * @param degrees
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryDegrees")
    @ApiOperation(value = "条件查询学历", notes = "传入：页码（nowPage必传），学历名称（name）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryBusiness(Degrees degrees, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Degrees> businessList = degreesService.conditionQueryDegrees(degrees, start, pageSize);
            if (businessList.size() > 0) {
                return ResultUtil.successResponse(businessList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询学历总数
     * @param degrees
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询学历总数", notes = "传入：学历名称（name）")
    public ResponseEntity<Result> count(Degrees degrees) {
        return ResultUtil.successResponse(degreesService.count(degrees));
    }

    /**
     *  添加学历
     * @param degrees
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加学历", notes = "传入：学历名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Degrees degrees) {
        if (StringUtils.isNotEmpty(degrees.getName())) {
            List<Degrees> degreesList = degreesService.findBySortMax();
            if (degreesList.size() > 0) {
                degrees.setSort(degreesList.get(0).getSort() + 1);
            } else {
                degrees.setSort(1);
            }
            if (degreesService.insert(degrees) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新学历
     * @param degrees
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新学历", notes = "传入：学历id（bdDegreesId），学历名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Degrees degrees) {
        if (StringUtils.isNotEmpty(degrees.getName()) && StringUtils.isNotEmpty(degrees.getBdDegreesId())) {
            Degrees degrees1 = degreesService.selectOne(new Degrees(degrees.getBdDegreesId()));
            if (null != degrees1) {
                degrees1.setName(degrees.getName());
                if (degreesService.update(degrees1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除学历
     * @param degrees
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除学历", notes = "传入：学历id（bdDegreesId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Degrees degrees) {
        if (StringUtils.isNotEmpty(degrees.getBdDegreesId())) {
            if (degreesService.delete(degrees) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param bdDegreesId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（bdDegreesId)(必传)")
    @ApiImplicitParam(name = "bdDegreesId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String bdDegreesId) {
        return move(bdDegreesId, 1);
    }

    /**
     *  下移
     * @param bdDegreesId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（bdDegreesId)(必传)")
    @ApiImplicitParam(name = "bdDegreesId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String bdDegreesId) {
        return move(bdDegreesId, -1);
    }

    /**
     *  上移下移复用
     * @param bdDegreesId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String bdDegreesId, Integer move) {
        if (StringUtils.isNotEmpty(bdDegreesId)) {
            List<Degrees> businessList = degreesService.findBySortMax();
            if (null == businessList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (businessList.size() > 1) {
                Integer code = degreesService.move(bdDegreesId, businessList, move);
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
