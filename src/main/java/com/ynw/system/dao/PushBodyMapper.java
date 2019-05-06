package com.ynw.system.dao;

import com.ynw.system.entity.PushBody;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PushBodyMapper extends MyMapper<PushBody> {

    List<PushBody> conditionQueryPshBody( @Param("body") PushBody body, @Param("pageSize") Integer pageSize, @Param("start") Integer start);

    Integer count(PushBody body);

}
