package com.ynw.system.dao;

import com.ynw.system.entity.WordFilter;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WordFilterMapper extends MyMapper<WordFilter> {

    List<WordFilter> conditionQueryWordFilter(@Param("wordFilter") WordFilter wordFilter, @Param("start") Integer start,
                                            @Param("pageSize") Integer pageSize);

    Integer count(WordFilter paraConf);

}
