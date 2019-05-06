package com.ynw.system.controller;

import com.ynw.system.entity.City;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.CityService;
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
@RequestMapping("/ynw-ms/city")
@Api(tags = "城市接口",description = "省市查询及操作接口")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     *  获取省份全部城市
     * @return
     */
    @PostMapping("/findCityByProvince")
    @ApiOperation(value = "获取省份全部城市", notes = "传入：所属省份ID（bdProvinceId）")
    public ResponseEntity<Result> findCityAll(City city) {
        if (StringUtils.isNotEmpty(city.getBdProvinceId())) {
            List<City> cityList = cityService.conditionQueryDistrict(city);
            if (cityList.size() > 0) {
                return ResultUtil.successResponse(cityList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  添加城市
     * @param city
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加城市", notes = "传入：城市名称（name），所属省份ID（bdProvinceId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(City city) {
        if (StringUtils.isNotEmpty(city.getBdProvinceId()) && StringUtils.isNotEmpty(city.getName())) {
            List<City> cityList = cityService.findBySortMax(city);
            if (cityList.size() > 0) {
                if (null != cityList.get(0).getSort()) {
                    //数据库有数据并且最大的sort不为空
                    city.setSort(cityList.get(0).getSort() + 1);
                }
            }
            if (cityService.insert(city) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新城市
     * @param city
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新城市", notes = "传入：城市ID（bdCityId），城市名称（name），所属省份ID（bdProvinceId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(City city) {
        if (StringUtils.isNotEmpty(city.getName()) && StringUtils.isNotEmpty(city.getBdCityId())) {
            City city1 = cityService.selectOne(new City(city.getBdCityId()));
            if (null != city1) {
                city1.setName(city.getName());
                if (cityService.update(city1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除城市
     * @param city
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除城市", notes = "传入：城市名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(City city) {
        if (StringUtils.isNotEmpty(city.getBdCityId())) {
            if (cityService.delete(city) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param city 移动对象
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（bdCityId)(必传)")
    public ResponseEntity<Result> moveUp(City city) {
        return move(city, 1);
    }

    /**
     *  下移
     * @param city 移动对象
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（bdCityId)(必传)")
    public ResponseEntity<Result> moveDown(City city) {
        return move(city, -1);
    }

    /**
     *  上移下移复用
     * @param city 移动对象
     * @return
     */
    public ResponseEntity<Result> move(City city, Integer move) {
        if (StringUtils.isNotEmpty(city.getBdCityId()) && StringUtils.isNotEmpty(city.getBdProvinceId())) {
            List<City> cityList = cityService.findBySortMax(city);
            if (null == cityList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (cityList.size() > 1) {
                Integer code = cityService.move(city.getBdCityId(), cityList, move);
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
