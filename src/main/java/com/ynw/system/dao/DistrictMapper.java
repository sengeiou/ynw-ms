package com.ynw.system.dao;

import com.ynw.system.entity.District;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface DistrictMapper extends MyMapper<District> {

    List<District> conditionQueryDistrict(District district);

    List<District> findBySortMax(District district);

}
