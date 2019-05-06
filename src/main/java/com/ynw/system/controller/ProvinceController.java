package com.ynw.system.controller;

import com.ynw.system.entity.Province;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ProvinceService;
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
 *  @version 2018-12/10
 */
@RestController
@RequestMapping("/ynw-ms/province")
@Api(tags = "省份接口",description = "省份查询及操作接口")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    /**
     *  获取全部省份
     * @return
     */
    @PostMapping("/findProvinceAll")
    @ApiOperation(value = "获取全部省份")
    public ResponseEntity<Result> findProvinceAll() {
        List<Province> provinceList = provinceService.conditionQueryProvince();
        if (provinceList.size() > 0) {
            return ResultUtil.successResponse(provinceList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加省份
     * @param province
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加省份", notes = "传入：省份名称（provinceName），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Province province) {
        if (StringUtils.isNotEmpty(province.getProvinceName())) {
            List<Province> provinceList = provinceService.findBySortMax();
            if (provinceList.size() > 0) {
                if (null != provinceList.get(0).getSort()) {
                    //数据库有数据并且最大的sort不为空
                    province.setSort(provinceList.get(0).getSort() + 1);
                }
            }
            if (provinceService.insert(province) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新省份
     * @param province
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新省份", notes = "传入：省份ID（bdProvinceId），省份名称（provinceName），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Province province) {
        if (StringUtils.isNotEmpty(province.getProvinceName()) && StringUtils.isNotEmpty(province.getBdProvinceId())) {
            Province province1 = provinceService.selectOne(new Province(province.getBdProvinceId()));
            if (null != province1) {
                province1.setProvinceName(province.getProvinceName());
                if (provinceService.update(province1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除省份
     * @param province
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除省份", notes = "传入：省份ID（bdProvinceId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Province province) {
        if (StringUtils.isNotEmpty(province.getBdProvinceId())) {
            if (provinceService.delete(province) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param bdProvinceId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（bdProvinceId)(必传)")
    @ApiImplicitParam(name = "bdProvinceId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String bdProvinceId) {
        return move(bdProvinceId, 1);
    }

    /**
     *  下移
     * @param bdProvinceId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（bdProvinceId)(必传)")
    @ApiImplicitParam(name = "bdProvinceId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String bdProvinceId) {
        return move(bdProvinceId, -1);
    }

    /**
     *  上移下移复用
     * @param bdProvinceId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String bdProvinceId, Integer move) {
        if (StringUtils.isNotEmpty(bdProvinceId)) {
            List<Province> provinceList = provinceService.findBySortMax();
            if (null == provinceList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (provinceList.size() > 1) {
                Integer code = provinceService.move(bdProvinceId, provinceList, move);
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
