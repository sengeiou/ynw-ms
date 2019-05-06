package com.ynw.system.service;

import com.ynw.system.dao.DictionaryMapper;
import com.ynw.system.entity.Dictionary;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@Service
@Transactional
public class DictionaryService {

    @Resource
    private DictionaryMapper dictionaryMapper;

    /**
     *  根据组键获取字典
     * @param dictionary
     * @return
     */
    @Transactional(readOnly = true)
    public List<Dictionary> findDictionaryByGroupKey(Dictionary dictionary) {
        return dictionaryMapper.findDictionaryByGroupKey(dictionary);
    }

    /**
     *  查询所有组键
     * @return
     */
    @Transactional(readOnly = true)
    public List<Dictionary> findDictionaryByGroupKy() {
        return dictionaryMapper.findDictionaryByGroupKy();
    }

    /**
     *  根据条件查询分页数据
     * @param dictionary
     * @param pageSize
     * @param start
     * @return
     */
    @Transactional(readOnly = true)
    public List<Dictionary> conditionQueryDictionary(Dictionary dictionary, Integer pageSize, Integer start) {
        return dictionaryMapper.conditionQueryDictionary(dictionary, pageSize, start);
    }

    /**
     *  根据条件数据总数
     * @param dictionary
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Dictionary dictionary) {
        return dictionaryMapper.count(dictionary);
    }

    /**
     *  根据条件查询当数据
     * @param dictionary
     * @return
     */
    public Dictionary selectOne(Dictionary dictionary) {
        return dictionaryMapper.selectOne(dictionary);
    }

    /**
     *  添加字典
     * @param dictionary
     * @return
     */
    public Integer insert(Dictionary dictionary) {
        dictionary.preInsert();
        return dictionaryMapper.insert(dictionary);
    }

    /**
     *  更新字典
     * @param dictionary
     * @return
     */
    public Integer update(Dictionary dictionary) {
        dictionary.preInsert();
        return dictionaryMapper.updateByPrimaryKey(dictionary);
    }

    /**
     *  删除字典
     * @param dictionary
     * @return
     */
    public Integer delete(Dictionary dictionary) {
        try {
            return dictionaryMapper.delete(dictionary);
        } catch (Exception e) {
            throw new MyException(ResultEnums.DATA_FORMANT_ERROR);
        }
    }

}
