package com.ynw.system.controller;

import com.ynw.system.entity.City;
import com.ynw.system.entity.District;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.CityService;
import com.ynw.system.service.DistrictService;
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
@RequestMapping("/ynw-ms/district")
@Api(tags = "县区接口",description = "县区查询及操作接口")
public class DistrictController {

    @Autowired
    private DistrictService districtService;

    /**
     *  获取城市全部县区
     * @return
     */
    @PostMapping("/findDistrictByCityId")
    @ApiOperation(value = "获取城市全部县区", notes = "传入：所属城市id（bdCityId）")
    public ResponseEntity<Result> findDistrictByCityId(District district) {
        if (StringUtils.isNotEmpty(district.getBdCityId())) {
            List<District> districtList = districtService.conditionQueryDistrict(district);
            if (districtList.size() > 0) {
                return ResultUtil.successResponse(districtList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加县区
     * @param district
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加县区", notes = "传入：区县名称（name），所属城市id（bdCityId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(District district) {
        if (StringUtils.isNotEmpty(district.getName()) && StringUtils.isNotEmpty(district.getBdCityId())) {
            List<District> districtList = districtService.findBySortMax(district);
            if (districtList.size() > 0) {
                if (null != districtList.get(0).getSort()) {
                    //数据库有数据并且最大的sort不为空
                    district.setSort(districtList.get(0).getSort() + 1);
                }
            }
            if (districtService.insert(district) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新县区
     * @param district
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新县区", notes = "传入：区县id(bdDistrictId),区县名称（name），所属城市id（bdCityId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(District district) {
        if (StringUtils.isNotEmpty(district.getName()) && StringUtils.isNotEmpty(district.getBdDistrictId())) {
            District district1 = districtService.selectOne(new District(district.getBdDistrictId()));
            if (null != district1) {
                district1.setName(district.getName());
                if (districtService.update(district1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除县区
     * @param district
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除县区", notes = "传入：区县id(bdDistrictId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(District district) {
        if (StringUtils.isNotEmpty(district.getBdDistrictId())) {
            if (districtService.delete(district) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param district 移动对象
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（bdDistrictId)(必传)")
    public ResponseEntity<Result> moveUp(District district) {
        return move(district, 1);
    }

    /**
     *  下移
     * @param district 移动对象
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（bdDistrictId)(必传)")
    public ResponseEntity<Result> moveDown(District district) {
        return move(district, -1);
    }

    /**
     *  上移下移复用
     * @param district 移动对象
     * @return
     */
    public ResponseEntity<Result> move(District district, Integer move) {
        if (StringUtils.isNotEmpty(district.getBdCityId()) && StringUtils.isNotEmpty(district.getBdDistrictId())) {
            List<District> districtList = districtService.findBySortMax(district);
            if (null == districtList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (districtList.size() > 1) {
                Integer code = districtService.move(district.getBdDistrictId(), districtList, move);
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
