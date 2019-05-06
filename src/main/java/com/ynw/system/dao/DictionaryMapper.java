package com.ynw.system.dao;

import com.ynw.system.entity.Dictionary;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictionaryMapper extends MyMapper<Dictionary> {

    List<Dictionary> findDictionaryByGroupKey(Dictionary dictionary);

    List<Dictionary> conditionQueryDictionary(@Param("dictionary") Dictionary dictionary, @Param("pageSize") Integer pageSize, @Param("start") Integer start);

    Integer count(Dictionary dictionary);

    List<Dictionary> findDictionaryByGroupKy();

}
