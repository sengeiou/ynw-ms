package com.ynw.system.dao;

import com.ynw.system.entity.Province;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface ProvinceMapper extends MyMapper<Province> {

    List<Province> findBySortMax();

    List<Province> conditionQueryProvince();

}
