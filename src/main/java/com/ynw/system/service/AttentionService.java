package com.ynw.system.service;

import com.ynw.system.dao.AttentionMapper;
import com.ynw.system.entity.AcUser;
import com.ynw.system.entity.Attention;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@Service
@Transactional
public class AttentionService {

    @Autowired
    private AttentionMapper attentionMapper;

    /**
     *  条件查询关注关系
     * @param acUser
     * @param provinceId 省份编号
     * @param start
     * @param pageSize
     * @return
     */
    public List<Attention> conditionQueryAttention(AcUser acUser, String provinceId, Integer start, Integer pageSize) {
        return attentionMapper.conditionQueryAttention(acUser, provinceId, start, pageSize);
    }

    /**
     *  条件查询数据总数
     * @param acUser
     * @param provinceId
     * @return
     */
    public Integer count(AcUser acUser, String provinceId) {
        return attentionMapper.count(acUser, provinceId);
    }

    /**
     *  查询当天建立关注数
     * @param nowTime
     * @return
     */
    public Integer nowCount(String nowTime) {
        return attentionMapper.nowCount(nowTime);
    }


}
