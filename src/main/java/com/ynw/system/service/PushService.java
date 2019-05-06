package com.ynw.system.service;

import com.ynw.system.dao.*;
import com.ynw.system.entity.*;
import com.ynw.system.util.JiGuangPushUtil;
import com.ynw.system.util.ThreadUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PushService {

    @Resource
    private PushBodyMapper pushBodyMapper;
    @Resource
    private PushBusinessMapper pushBusinessMapper;
    @Resource
    private PushSendMapper pushSendMapper;
    @Resource
    private PushTargetMapper pushTargetMapper;
    @Resource
    private AcUserMapper acUserMapper;
    @Resource
    private AdviceMapper adviceMapper;

    /**
     *  根据条件分页查询数据
     * @param body
     * @param pageSize
     * @param start
     * @return
     */
    @Transactional(readOnly = true)
    public List<PushBody> conditionQueryPshBody(PushBody body, Integer pageSize, Integer start) {
        return pushBodyMapper.conditionQueryPshBody(body, pageSize, start);
    }

    /**
     *  根据条件查询数据总数
     * @param body
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(PushBody body) {
        return pushBodyMapper.count(body);
    }

    /**
     *  根据条件查询用户反馈数据
     * @param advice
     * @param pageSize
     * @param start
     * @return
     */
    @Transactional(readOnly = true)
    public List<Advice> conditionQueryAdvice(Advice advice, Integer pageSize, Integer start, Integer reply) {
        return adviceMapper.conditionQueryAdvice(advice, pageSize, start, reply);
    }

    /**
     *  根据条件查询数据总数
     * @param advice
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Advice advice, Integer reply) {
        return adviceMapper.count(advice, reply);
    }

    /**
     * 插入消息主体
     * @param infoOne 消息关联编号
     * @param infoTwo 消息关联编号
     * @param mgBody
     * @param type
     * @param acUserId 发送给谁
     */
    public void insertMgBody(String infoOne, String infoTwo, boolean isPush,PushBody mgBody,String type,String... acUserId){
        Date nowTime= new Date();//获取现在的时间
        mgBody.preInsert();
        if( pushBodyMapper.insert(mgBody)<1)
            throw new RuntimeException("数据插入错误");
        pushBusinessMapper.insert(new PushBusiness(mgBody.getMgBodyId(),type,infoOne,infoTwo));//插入到消息业务表中去
        PushSend mgSend=new PushSend(mgBody.getMgBodyId());
        if((mgBody.getBeginTime()==null&&mgBody.getEndTime()==null)||(mgBody.getBeginTime().before(nowTime)&&mgBody.getEndTime().after(nowTime))){//如果现在在开始和结束时间之间就开始发送消息
            if(acUserId[0].equals("all")){//如果是设置为全部推送
                List<AcUser> users=acUserMapper.selectAll();//查询所有的好友
                if (isPush) {
                    pushMsg("all", mgBody, mgSend);
                }
                mgSend.setSendTime(nowTime);
                mgSend.setStatus(1);
                mgSend.setTimer(new Timestamp(0));
                pushSendMapper.insert(mgSend);
                foreachUser(mgBody, users);
            }else {//否则按照别名推送
                if (isPush) {
                    pushMsg("alias", mgBody, mgSend, acUserId);
                }
                mgSend.setSendTime(nowTime);
                mgSend.setStatus(1);
                mgSend.setTimer(new Timestamp(0));
                pushSendMapper.insert(mgSend);
                foreachAcUser(mgBody, acUserId);//把发送推送的用户插入到mgTarget表中去
            }
        }else if(mgBody.getBeginTime().after(nowTime)){//如果还没开始则计时到时间后再发送消息体
            Timestamp timestamp=new Timestamp(mgBody.getBeginTime().getTime()-nowTime.getTime());
            mgSend.setStatus(1);
            mgSend.setTimer(timestamp);
            pushSendMapper.insert(mgSend);

            ThreadUtil.execute(() -> {//在指定定时器的时间之后,把消息的状态转变成已发送
                try {
                    Thread.sleep(timestamp.getTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(acUserId[0].equals("all")) {
                    //并且进行推送 JPush位置预留代码
                    if (isPush) {
                        pushMsg("all", mgBody, mgSend);
                    }
                    mgSend.setSendTime(new Date());
                    pushSendMapper.updateByPrimaryKeySelective(mgSend);
                    List<AcUser> users=acUserMapper.selectAll();//查询所有的好友
                    foreachUser(mgBody, users);

                }else{

                    //并且进行推送 JPush位置预留代码
                    if (isPush) {
                        pushMsg("alias", mgBody, mgSend, acUserId);
                    }
                    mgSend.setSendTime(new Date());
                    pushSendMapper.updateByPrimaryKeySelective(mgSend);
                    foreachAcUser(mgBody, acUserId);
                }

            });
        }

    }

    /**
     * 根据mgBody和mgSend进行推送
     * @param type 推送类型
     * @param mgBody 推送主体
     * @param mgSend 推送发送表
     * @param acUserId 推送用户的范围数组
     */
    private void pushMsg(String type,PushBody mgBody, PushSend mgSend, String... acUserId) {
        Map alert=new LinkedHashMap();
        alert.put("title",mgBody.getTitle());
        alert.put("body",mgBody.getContent());
        JiGuangPushUtil.pushMsg(type, alert, StringUtils.isEmpty(mgBody.getTargetModuleName())?"PMTM_CLERK":mgBody.getTargetModuleName(), acUserId);
    }

    /**
     * 遍历用户ID数组把推送消息插入到消息目标表中去
     * @param mgBody
     * @param acUserId
     */
    private void foreachAcUser(PushBody mgBody, String[] acUserId) {
        for (String s : acUserId) {
            Integer not=0;
            if("PMG_FRIEND".equals(mgBody.getGroup())||"PMT_MODULE".equals(mgBody.getType())){
                not=1;
            }
            pushTargetMapper.insert(new PushTarget(mgBody.getMgBodyId(), s,not));
        }
    }

    private void foreachUser(PushBody mgBody, List<AcUser> users) {
        for (AcUser s : users) {
            Integer not=0;
            if("PMG_FRIEND".equals(mgBody.getGroup())||"PMT_MODULE".equals(mgBody.getType())){
                not=1;
            }
            pushTargetMapper.insert(new PushTarget(mgBody.getMgBodyId(),s.getAcUserId(),not));
        }
    }

}
