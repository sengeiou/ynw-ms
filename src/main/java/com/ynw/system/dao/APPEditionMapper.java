package com.ynw.system.dao;

import com.ynw.system.entity.APPEdition;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface APPEditionMapper extends MyMapper<APPEdition> {

    List<APPEdition> conditionQueryAPPEdition(@Param("appEdition") APPEdition appEdition, @Param("start") Integer start,
                                              @Param("pageSize") Integer pageSize);

    Integer count(APPEdition appEdition);

}
