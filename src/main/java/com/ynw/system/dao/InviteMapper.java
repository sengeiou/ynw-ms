package com.ynw.system.dao;

import com.ynw.system.entity.Invite;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  约活动报名用户mapper层
 */
public interface InviteMapper extends MyMapper<Invite> {

    /**
     *  条件分页查询数据
     * @param invite
     * @param start
     * @param pageSize
     * @return
     */
    List<Invite> conditionQueryInvite(@Param("invite") Invite invite, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    /**
     *  条件查询总数据量
     * @param invite
     * @return
     */
    Integer count(Invite invite);

}
