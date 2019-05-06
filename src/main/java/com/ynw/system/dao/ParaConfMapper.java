package com.ynw.system.dao;

import com.ynw.system.entity.ParaConf;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ParaConfMapper extends MyMapper<ParaConf> {

    List<ParaConf> conditionQueryParaConf(@Param("paraConf") ParaConf paraConf, @Param("start") Integer start,
                                          @Param("pageSize") Integer pageSize);

    Integer count(ParaConf paraConf);

//    Integer definedInsert(ParaConf paraConf);
//
//    Integer definedUpdate(ParaConf paraConf);

    ParaConf findByKey(String key);

}
