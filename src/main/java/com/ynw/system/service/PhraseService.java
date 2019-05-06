package com.ynw.system.service;

import com.ynw.system.dao.PhraseMapper;
import com.ynw.system.entity.Channel;
import com.ynw.system.entity.Phrase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PhraseService extends MyService<PhraseMapper, Phrase> {

    /**
     *  根据条件分页查询数据
     * @param phrase
     * @param pageSize
     * @param start
     * @return
     */
    @Transactional(readOnly = true)
    public List<Phrase> conditionQueryPhrase(Phrase phrase, Integer pageSize, Integer start) {
        return dao.conditionQueryPhrase(phrase, pageSize, start);
    }

}
