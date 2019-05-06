package com.ynw.system.service;

import com.ynw.system.dao.ParaConfMapper;
import com.ynw.system.entity.ParaConf;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/5
 */
@Service
@Transactional
public class ParaConfService extends MyService<ParaConfMapper, ParaConf> {

    /**
     *  条件查询
     * @param paraConf
     * @param start
     * @param pageSize
     * @return
     */
    public List<ParaConf> conditionQueryParaConf(ParaConf paraConf, Integer start, Integer pageSize) {
        return dao.conditionQueryParaConf(paraConf, start, pageSize);
    }

    /**
     *  条件查询总数据数
     * @param paraConf
     * @return
     */
    public Integer count(ParaConf paraConf) {
        return dao.count(paraConf);
    }

    /**
     *  插入数据
     * @param paraConf
     * @return
     */
//    public Integer insertParaConf(ParaConf paraConf) {
//        paraConf.preInsert();
//        return dao.definedInsert(paraConf);
//    }

    /**
     *  更新数据
     * @param paraConf
     * @return
     */
//    public Integer updateParaConf(ParaConf paraConf) {
//        return dao.definedUpdate(paraConf);
//    }

    /**
     *  通过key值查询
     * @param key
     * @return
     */
    public ParaConf findByKey(String key) {
        return dao.findByKey(key);
    }

}
