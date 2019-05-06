package com.ynw.system.dao;

import com.ynw.system.entity.City;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface CityMapper extends MyMapper<City> {

    List<City> conditionQueryCity(City city);

    List<City> findBySortMax(City city);

}
