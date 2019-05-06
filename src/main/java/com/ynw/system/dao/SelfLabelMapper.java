package com.ynw.system.dao;

import com.ynw.system.entity.SelfLabel;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SelfLabelMapper extends MyMapper<SelfLabel> {

    List<SelfLabel> conditionQuerySelfLabel(@Param("selfLabel") SelfLabel selfLabel, @Param("start") Integer start,
                                          @Param("pageSize") Integer pageSize);

    Integer count(SelfLabel selfLabel);

    List<SelfLabel> findBySortMax();

    List<SelfLabel> findSelfLabelByAcUserId(String acUserId);

}
