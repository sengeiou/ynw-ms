package com.ynw.system.service;

import com.ynw.system.dao.CityMapper;
import com.ynw.system.entity.City;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/8
 */
@Service
@Transactional
public class CityService extends MyService<CityMapper, City> {

    /**
     * 根据省份查询城市
     * @param city
     * @return
     */
    public List<City> conditionQueryDistrict(City city) {
        return dao.conditionQueryCity(city);
    }

    /**
     *  根据省份获取降序数据
     * @return
     */
    public List<City> findBySortMax(City city) {
        return dao.findBySortMax(city);
    }

    /**
     *  上移下移复用
     * @param bdCityId 移动对象编号
     * @param cityList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String bdCityId, List<City> cityList, Integer move) {
        Integer code = 0;
        Integer size = cityList.size();
        for (int i = 0; i < size; i++
        ) {
            if (cityList.get(i).getBdCityId().equals(bdCityId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = cityList.get(i + move).getSort();
                    cityList.get(i + move).setSort(cityList.get(i).getSort());
                    code = this.update(cityList.get(i + move));
                    if (code > 0) {
                        cityList.get(i).setSort(sort);
                        code = this.update(cityList.get(i));
                    }
                }
                break;
            }
        }
        if (code < 1) {
            //抛出一个错误保证回滚
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

}
