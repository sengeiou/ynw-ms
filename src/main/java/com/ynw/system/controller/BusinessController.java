package com.ynw.system.controller;

import com.ynw.system.entity.Business;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.BusinessService;
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
@RequestMapping("/ynw-ms/business")
@Api(tags = "系统行业接口",description = "行业查询及操作接口")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    /**
     *  条件查询行业
     * @param business
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryBusiness")
    @ApiOperation(value = "条件查询行业", notes = "传入：页码（nowPage必传），行业名称（name）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryBusiness(Business business, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Business> businessList = businessService.conditionQueryBusiness(business, start, pageSize);
            if (businessList.size() > 0) {
                return ResultUtil.successResponse(businessList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询行业总数
     * @param business
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询行业总数", notes = "传入：行业名称（name）")
    public ResponseEntity<Result> count(Business business) {
        return ResultUtil.successResponse(businessService.count(business));
    }

    /**
     *  添加行业
     * @param business
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加行业", notes = "传入：行业名称（name）,添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Business business) {
        if (StringUtils.isNotEmpty(business.getName())) {
            List<Business> businessList = businessService.findBySortMax();
            if (businessList.size() > 0) {
                business.setSort(businessList.get(0).getSort() + 1);
            } else {
                business.setSort(1);
            }
            if (businessService.insert(business) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新行业
     * @param business
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新行业", notes = "传入：行业id（bdBusinessId）,行业名称（name）,添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Business business) {
        if (StringUtils.isNotEmpty(business.getName()) && StringUtils.isNotEmpty(business.getBdBusinessId())) {
            Business business1 = businessService.selectOne(new Business(business.getBdBusinessId()));
            if (null != business1) {
                business1.setName(business.getName());
                if (businessService.update(business1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除行业
     * @param business
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除行业", notes = "传入：行业id（bdBusinessId）,添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Business business) {
        if (StringUtils.isNotEmpty(business.getBdBusinessId())) {
            if (businessService.delete(business) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param bdBusinessId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（bdBusinessId)(必传)")
    @ApiImplicitParam(name = "bdBusinessId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String bdBusinessId) {
        return move(bdBusinessId, 1);
    }

    /**
     *  下移
     * @param bdBusinessId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（bdBusinessId)(必传)")
    @ApiImplicitParam(name = "bdBusinessId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String bdBusinessId) {
        return move(bdBusinessId, -1);
    }

    /**
     *  上移下移复用
     * @param bdBusinessId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String bdBusinessId, Integer move) {
        if (StringUtils.isNotEmpty(bdBusinessId)) {
            List<Business> businessList = businessService.findBySortMax();
            if (null == businessList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (businessList.size() > 1) {
                Integer code = businessService.move(bdBusinessId, businessList, move);
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
