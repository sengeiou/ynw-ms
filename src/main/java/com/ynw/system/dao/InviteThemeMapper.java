package com.ynw.system.dao;

import com.ynw.system.entity.InviteTheme;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  约活动主题mapper层
 */
public interface InviteThemeMapper extends MyMapper<InviteTheme> {

    /**
     *  根据编号更新数据
     * @param inviteTheme
     * @return
     */
    Integer update(InviteTheme inviteTheme);

    /**
     * 获取最大排序的数据
     * @return
     */
    InviteTheme findMaxSort();

    /**
     *  条件分页查询数据
     * @param inviteTheme
     * @param start
     * @param pageSize
     * @return
     */
    List<InviteTheme> conditionQueryInviteTheme(@Param("inviteTheme") InviteTheme inviteTheme, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    /**
     *  条件查询数据总数
     * @param inviteTheme
     * @return
     */
    Integer count(InviteTheme inviteTheme);

}
