package com.ynw.system.service;

import com.ynw.system.dao.WordFilterMapper;
import com.ynw.system.entity.WordFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@Service
@Transactional
public class WordFilterService extends MyService<WordFilterMapper, WordFilter> {

    /**
     *  条件查询
     * @param wordFilter
     * @param start
     * @param pageSize
     * @return
     */
    public List<WordFilter> conditionQueryWordFilter(WordFilter wordFilter, Integer start, Integer pageSize) {
        return dao.conditionQueryWordFilter(wordFilter, start, pageSize);
    }

    /**
     *  条件查询总数据数
     * @param wordFilter
     * @return
     */
    public Integer count(WordFilter wordFilter) {
        return dao.count(wordFilter);
    }

}
