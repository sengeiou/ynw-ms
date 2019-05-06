package com.ynw.system.service;

import com.ynw.system.dao.FriendMapper;
import com.ynw.system.entity.AcUser;
import com.ynw.system.entity.Friend;
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
public class FriendService {

    @Autowired
    private FriendMapper friendMapper;

    /**
     *  条件查询用户关系
     * @param acUser
     * @param provinceId 省份编号
     * @param start
     * @param pageSize
     * @return
     */
    public List<Friend> conditionQueryFriend(AcUser acUser, String provinceId, Integer start, Integer pageSize) {
        return friendMapper.conditionQueryFriend(acUser, provinceId, start, pageSize);
    }

    /**
     *  条件查询数据总数
     * @param acUser
     * @param provinceId
     * @return
     */
    public Integer count(AcUser acUser, String provinceId) {
        return friendMapper.count(acUser, provinceId);
    }

    /**
     *  查询当天建立好友数
     * @param nowTime
     * @return
     */
    public Integer nowCount(String nowTime) {
        return friendMapper.nowCount(nowTime);
    }


}
