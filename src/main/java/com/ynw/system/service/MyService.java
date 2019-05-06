package com.ynw.system.service;

import com.ynw.system.entity.DateEntity;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import com.ynw.system.util.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ChengZhi
 * @version 2018/11/24
 */

public abstract class MyService<D extends MyMapper<T>, T extends DateEntity> {

    @Autowired
    protected D dao;

    /**
     * 获取单条数据
     * @return
     */
//    @Cacheable("selectOne")
    @Transactional(readOnly = true)
    public T selectOne(T entity) {
        return dao.selectOne(entity);
    }

    /**
     * 根据条件查询对象
     * @param entity
     * @return
     */
//    @Cacheable("selectList")
    @Transactional(readOnly = true)
    public List<T> selectList(T entity){
        return dao.select(entity);
    }

    /**
     *查询所有
     * @return
     */
//    @Cacheable("selectAll")
    @Transactional(readOnly = true)
    public List<T> selectAll(){
        return dao.selectAll();
    }

    /**
     * 查询总条数
     * @param entity
     * @return
     */
//    @Cacheable("count")
    @Transactional(readOnly = true)
    public int selectCount(T entity){
        return dao.selectCount(entity);
    }

    /**
     * 插入一条对象数据
     * @param entity
     * @return
     */
//    @CacheEvict(cacheNames={"count","selectAll","selectOne","selectList","findByUser","findAll","conditionQuery","conditionQueryAll"/*可以接多个cacheNames，格式{“loginCache”，“”，“”，、、、}*/},
//            allEntries=true)
    public int insert(T entity){
        entity.preInsert();
        return dao.insert(entity);
    }

    /**
     * 批量插入信息
     * @param list
     * @return
     */
//    @CacheEvict(cacheNames={"count","selectAll","selectList","selectOnt","findByUser","findAll","conditionQuery","conditionQueryAll"/*可以接多个cacheNames，格式{“loginCache”，“”，“”，、、、}*/},
//            allEntries=true)
    public int insertList(List<T> list){
        return dao.insertList(list);
    }

    /**
     * 根据主键修改对象信息
     * @param entity
     * @return
     */
//    @CacheEvict(cacheNames={"count","selectAll","selectList","selectOnt","findByUser","findAll","conditionQuery","conditionQueryAll"/*可以接多个cacheNames，格式{“loginCache”，“”，“”，、、、}*/},
//            allEntries=true)
    public int update(T entity){
        entity.preUpdate();
        return dao.updateByPrimaryKey(entity);
    }

    /**
     * 物理删除数据
     * @param entity
     * @return
     */
//    @CacheEvict(cacheNames={"count","selectAll","selectList","selectOnt","findByUser","findAll","conditionQuery","conditionQueryAll"/*可以接多个cacheNames，格式{“loginCache”，“”，“”，、、、}*/},
//            allEntries=true)
    public int delete(T entity){
        try {
            return dao.delete(entity);
        } catch (Exception e) {
            throw new MyException(ResultEnums.DATA_FORMANT_ERROR);
        }
    }


}
