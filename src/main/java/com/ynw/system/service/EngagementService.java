package com.ynw.system.service;

import com.ynw.system.dao.EngagementMapper;
import com.ynw.system.dao.InviteMapper;
import com.ynw.system.entity.Engagement;
import com.ynw.system.entity.Invite;
import com.ynw.system.entity.PushBody;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *  约活动service层
 */
@Service
@Transactional
public class EngagementService extends MyService<EngagementMapper, Engagement> {

    @Autowired
    private PushService pushService;
    @Resource
    private InviteMapper inviteMapper;

    /**
     * 条件分页查询数据
     * @param invite
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly=true)
    public List<Invite> conditionQueryInvite(Invite invite, Integer start, Integer pageSize) {
        return inviteMapper.conditionQueryInvite(invite, start, pageSize);
    }

    /**
     *  条件查询总数据量
     * @param invite
     * @return
     */
    @Transactional(readOnly=true)
    public Integer count(Invite invite) {
        return inviteMapper.count(invite);
    }

    /**
     *  更新活动审核状态
     * @param engagement
     * @param reason
     * @return
     */
    public Integer updateCheckStatus(Engagement engagement, String reason) {
        Integer code = dao.updateCheckStatus(engagement);
        if (code > 0) {
            String content = "";
            String title = "";
            if (StringUtils.isNotEmpty(reason)) {
                title = "活动审核不通过";
                content = "您的活动不通过！！！（" + reason + ")";
            } else {
                title = "活动审核通过";
                content = "您的活动审核通过！！！";
            }
            Invite invite = inviteMapper.selectOne(new Invite(engagement.getAtRegisterId(), 1));
            if (null == invite)
                throw new MyException(ResultEnums.NO_DATA_ERROR);
            PushBody body = new PushBody("PMT_TEXT","PMG_SYSTEM","PMS_SINGLE", title,
                    content);
            pushService.insertMgBody(null, null, true, body, "PMBT_PLATFORM_TO_REMIND", invite.getAcUserId());
        }
        return code;
    }

    /**
     *  根据编号查询数据
     * @param registerId
     * @return
     */
    @Transactional(readOnly = true)
    public Engagement findById(String registerId) {
        return dao.findById(registerId);
    }

    /**
     * 条件分页查询数据
     * @param engagement
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<Engagement> conditionQueryEngagement(Engagement engagement, Integer start, Integer pageSize) {
        return dao.conditionQueryEngagement(engagement, pageSize, start);
    }

    /**
     *  条件查询数据总数
     * @param engagement
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Engagement engagement) {
        return dao.count(engagement);
    }

}
