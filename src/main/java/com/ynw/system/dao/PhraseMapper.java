package com.ynw.system.dao;

import com.ynw.system.entity.Channel;
import com.ynw.system.entity.Phrase;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhraseMapper extends MyMapper<Phrase> {

    List<Phrase> conditionQueryPhrase(@Param("phrase") Phrase phrase, @Param("pageSize") Integer pageSize, @Param("start") Integer start);

}
