package com.ynw.system.dao;

import com.ynw.system.entity.RegisterClassify;
import com.ynw.system.util.MyMapper;

import java.util.List;

public interface RegisterClassifyMapper extends MyMapper<RegisterClassify> {

    List<RegisterClassify> conditionQueryRegisterClassify();

}
