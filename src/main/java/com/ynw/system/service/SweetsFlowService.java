package com.ynw.system.service;

import com.ynw.system.dao.InviteTaskMapper;
import com.ynw.system.dao.SweetsFlowMapper;
import com.ynw.system.dao.SweetsSumMapper;
import com.ynw.system.entity.InviteTask;
import com.ynw.system.entity.PushBody;
import com.ynw.system.entity.SweetsFlow;
import com.ynw.system.entity.SweetsSum;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SweetsFlowService extends MyService<SweetsFlowMapper, SweetsFlow> {

    @Resource
    private SweetsSumMapper sweetsSumMapper;
    @Resource
    private InviteTaskMapper inviteTaskMapper;
    @Autowired
    private PushService pushService;

    /**
     * 推送提现失败消息
     * @param userId 用户id
     * @param content
     * @param quantity
     */
    public void withdrawDeposit(String userId, String content, Integer quantity, String acSugarFlowId) {
        SweetsSum sweetsSum = sweetsSumMapper.selectOne(new SweetsSum(userId));
        sweetsSum.setSum(sweetsSum.getSum() + quantity);
        Integer code = sweetsSumMapper.updateSum(sweetsSum);
        if (code > 0) {
            SweetsFlow flow = this.dao.findById(acSugarFlowId);
            flow.setAssoBusinessKey("GOLD_CONVERSION_CANCEL");
            code = this.update(flow);
            if (code > 0) {
                PushBody body = new PushBody("PMT_TEXT","PMG_SYSTEM","PMS_SINGLE","提现申请不通过",
                        "您的提现申请不通过！！！（"+content+")");
                pushService.insertMgBody(null, null, true, body, "PMBT_PLATFORM_TO_REMIND", userId);
            }
        }
    }

    /**
     *  条件分页查询数据
     * @param inviteTask
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<InviteTask> conditionQueryInviteTask(InviteTask inviteTask,
                                                    Integer start, Integer pageSize) {
        return inviteTaskMapper.conditionQueryInviteTask(inviteTask, pageSize, start);
    }

    /**
     *  符合条件的总数
     * @param inviteTask
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(InviteTask inviteTask) {
        return inviteTaskMapper.count(inviteTask);
    }

    /**
     *  条件分页查询数据
     * @param sweetsSum
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<SweetsSum> conditionQuerySweetsSum(SweetsSum sweetsSum,
                                                     Integer start, Integer pageSize) {
        return sweetsSumMapper.conditionQuerySweetsSum(sweetsSum, start, pageSize);
    }

    /**
     *  符合条件的总数
     * @param sweetsSum
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(SweetsSum sweetsSum) {
        return sweetsSumMapper.count(sweetsSum);
    }

    /**
     * 更新流水和糖豆总数
     * @param sweetsFlow
     * @return
     */
    public Integer updateType(SweetsFlow sweetsFlow) {
        Integer code = 0;
        SweetsFlow flow = this.dao.findById(sweetsFlow.getAcSugarFlowId());
        if (null != flow) {
            flow.setAssoBusinessKey("GOLD_CONVERSION_YET");
            flow.setQuantity(sweetsFlow.getQuantity());
            code = this.update(flow);
            if (code > 0) {
                SweetsSum sweetsSum = sweetsSumMapper.selectOne(new SweetsSum(flow.getAcUserId()));
                if (null != sweetsSum) {
                    code = sweetsSumMapper.updateSumAndMoney(new SweetsSum(flow.getAcUserId(), sweetsSum.getSum()-sweetsFlow.getQuantity(),
                            sweetsSum.getGoldConversion() + sweetsFlow.getQuantity()/100, new Date()));
                } else {
                    code = 0;
                }
            }
        }
        if (code == 0) {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

    /**
     *  根据id查询数据
     * @param acSugarFlowId
     * @return
     */
    @Transactional(readOnly = true)
    public SweetsFlow findById(String acSugarFlowId) {
        return dao.findById(acSugarFlowId);
    }

    /**
     *  条件分页查询数据
     * @param sweetsFlow
     * @param beginDate
     * @param endDate
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<SweetsFlow> conditionQuerySweetsFlow(SweetsFlow sweetsFlow, String beginDate, String endDate,
                                                     Integer start, Integer pageSize) {
        return dao.conditionQuerySweetsFlow(sweetsFlow, beginDate, endDate, start, pageSize);
    }

    /**
     *  符合条件的总数
     * @param sweetsFlow
     * @param beginDate
     * @param endDate
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(SweetsFlow sweetsFlow, String beginDate, String endDate) {
        return dao.count(sweetsFlow, beginDate, endDate);
    }

}
