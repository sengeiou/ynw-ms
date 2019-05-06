package com.ynw.system.service;

import com.ynw.system.dao.InviteThemeMapper;
import com.ynw.system.entity.InviteTheme;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 约活动主题service层
 */
@Service
@Transactional
public class InviteThemeService extends MyService<InviteThemeMapper, InviteTheme> {

    /**
     *  根据编号更新数据
     * @param inviteTheme
     * @return
     */
    public Integer updateById(InviteTheme inviteTheme) {
        return dao.update(inviteTheme);
    }

    /**
     * 获得排序最大的数据
     * @return
     */
    @Transactional(readOnly = true)
    public InviteTheme findMaxSort() {
        return dao.findMaxSort();
    }

    /**
     *  条件分页查询数据
     * @param inviteTheme
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<InviteTheme> conditionQueryInviteTheme(InviteTheme inviteTheme, Integer start, Integer pageSize) {
        return dao.conditionQueryInviteTheme(inviteTheme, start, pageSize);
    }

    /**
     * 条件查询数据总数
     * @param inviteTheme
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(InviteTheme inviteTheme) {
        return dao.count(inviteTheme);
    }

}
