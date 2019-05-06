package com.ynw.system.service;

import com.ynw.system.dao.APPEditionMapper;
import com.ynw.system.entity.APPEdition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@Service
@Transactional
public class APPEditionService extends MyService<APPEditionMapper, APPEdition> {

    /**
     *  条件获取app版本信息
     * @param appEdition
     * @param start
     * @param pageSize
     * @return
     */
    public List<APPEdition> conditionQueryResourceAll(APPEdition appEdition, Integer start, Integer pageSize) {
        return dao.conditionQueryAPPEdition(appEdition, start, pageSize);
    }

    /**
     *  条件统计数据数量
     * @param appEdition
     * @return
     */
    public Integer count(APPEdition appEdition) {
        return dao.count(appEdition);
    }

}
