package com.ynw.system.service;

import com.aliyuncs.exceptions.ClientException;
import com.ynw.system.controller.ParaConfController;
import com.ynw.system.dao.*;
import com.ynw.system.entity.*;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import com.ynw.system.util.DateUtils;
import com.ynw.system.util.SendSmsUtil;
import com.ynw.system.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class ActivityService {

    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private CpFlowMapper cpFlowMapper;
    @Resource
    private CpRelMapper cpRelMapper;
    @Autowired
    private AsyncService asyncService;
    @Autowired
    private HuanXinService huanXinService;
    @Autowired
    private AcUserMapper acUserMapper;
    @Resource
    private ImGroupMapper imGroupMapper;
    @Resource
    private RegisterMapper registerMapper;

    //活动字母编号
    private String[] matchNo = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    public Integer matchingBatchUpdate(List<Activity> list) {
        return activityMapper.matchingBatchUpdate(list);
    }

    /**
     *  创建环信cp大群和cp聊天
     * @param registerId
     */
    @Async
    public void chatRoom(String registerId) {
        List<CpRel> cpRelList = cpRelMapper.findByRegisterIdAndStatus(registerId);
        AcUser acUser = new AcUser();
        acUser.setType(1);
        String ownerId = acUserMapper.select(acUser).get(0).getImUserId();
        Map<String, List<String>> groupMember = new HashMap<>();
        String matchNo = "";
        List<String> userList = new ArrayList<>();//获取加入群聊的用户
        for (CpRel cpRel: cpRelList
             ) {
            List<String> members = new ArrayList<>();
            members.add(cpRel.getResUserImId());
            members.add(cpRel.getReqUserImId());
            huanXinService.createChatGroup(ownerId, cpRel.getReqUserName()+"&"+cpRel.getResUserName()+"的CP房间", "CP", true, 200,
                    false, members, "IGBT_WEEKCP", registerId, cpRel.getMatchNo(), 1);
            List<String> target = new ArrayList<>();
            ImGroup group = imGroupMapper.selectOne(new ImGroup("IGBT_WEEKCP", registerId, 1, cpRel.getMatchNo()));
            target.add(group.getHxGroupId());
            huanXinService.sendMessage("chatgroups", target, "txt", "您已经进入："+cpRel.getReqUserName()+"&"+cpRel.getResUserName()+"的CP房间", "一周情侣工作人员");
            matchNo = cpRel.getMatchNo().split(":")[0];
            if (!groupMember.containsKey(matchNo)) {
                groupMember.put(matchNo, new ArrayList<String>());
            }
            groupMember.get(matchNo).add(cpRel.getReqUserImId());
            groupMember.get(matchNo).add(cpRel.getResUserImId());
            userList.add(cpRel.getReqUserId());
            userList.add(cpRel.getResUserId());
        }
        if (groupMember.size() > 0) {
            String registerName = registerMapper.selectOne(new Register(registerId)).getName();
            Integer maxUsers = Integer.valueOf(ParaConfController.paraConfController.findUrlByKey("HUAN_XIN_GROUP_MAX"));//环信群最大成员数
            activityMapper.updateStatusByUserId(userList, registerId, 3);
            for (Map.Entry<String, List<String>> map: groupMember.entrySet()
                 ) {
                String groupName = registerName + map.getKey() + "群";//群名
                huanXinService.createChatGroup(ownerId, groupName, groupName, true, maxUsers,
                        false, map.getValue(), "IGBT_WEEKCP", registerId, null, 0);
                List<String> target = new ArrayList<>();
                ImGroup imGroup = imGroupMapper.selectOne(new ImGroup(groupName, "IGBT_WEEKCP", registerId, 1, 0));
                target.add(imGroup.getHxGroupId());
                huanXinService.sendMessage("chatgroups", target, "txt", "您已经进入："+ groupName, "一周情侣工作人员");
            }
        }
        System.out.println("-------------建群完成---------------");
    }

    /**
     *  活动未响应的用户剔除本次活动
     * @param atRegisterId 活动编号
     * @return
     */
    public Integer updateStatusByConfirmCp(String atRegisterId) {
        cpRelMapper.updateByRegisterIdOrStatus(atRegisterId);
        Integer code = activityMapper.updateStatusByConfirmCp(atRegisterId);
        if (code > 0) {
            activityMapper.updateMatchNoByConfirmCp(atRegisterId);
            activityMapper.updateMatchNoByStatus(atRegisterId);
        }
        return code;
    }

    /**
     *  批量发送短信
     * @param registerId
     * @return
     */
    public void findByRegisterIdAndLimit(String registerId) {
        List<Activity> activityList = activityMapper.selectListLimit(new Activity(registerId,3), 1000);
        updateStatus(activityList, 4);
    }

    /**
     *  根据活动编号和是否加入活动辅群查询用户
     * @param registerId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Activity> findByRegisterIdAndGroup(String registerId) {
        return activityMapper.findByRegisterIdAndGroup(registerId);
    }

    /**
     *  条件查询数据
     * @param activity
     * @return
     */
    @Transactional(readOnly = true)
    public List<Activity> selectList(Activity activity) {
        return activityMapper.select(activity);
    }

    @Transactional(readOnly = true)
    public List<Activity> selectListLimit(Activity activity, Integer pageSize) {
        return activityMapper.selectListLimit(activity, pageSize);
    }



    /**
     *  根据两个用户编号id查询是否存在cp关系
     * @param reqUserId
     * @param resUserId
     * @param registerId
     * @return
     */
    @Transactional(readOnly = true)
    public CpRel findByReqUserIdAndResUserIdAndRegisterId(String reqUserId, String resUserId, String registerId) {
        return cpRelMapper.findByReqUserIdAndResUserIdAndRegisterId(reqUserId, resUserId, registerId);
    }

    /**
     *  查询所有参加活动用户
     * @return
     */
    @Transactional(readOnly = true)
    public List<Activity> conditionQueryActivity(Activity activity, Integer pageSize, Integer start) {
        return activityMapper.conditionQueryActivity(activity,pageSize,start);
    }

    /**
     *  统计数量
     * @param activity
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Activity activity) {
        return activityMapper.count(activity);
    }

    /**
     *  条件查询单个数据
     * @param activityId
     * @return
     */
    @Transactional(readOnly = true)
    public Activity findActivity(String activityId) {
        return activityMapper.findActivity(activityId);
    }

    /**
     *  查询活动参与用户
     * @return
     */
    @Transactional(readOnly = true)
    public List<Activity> findAllByRegisterId(Activity activity) {
        return activityMapper.select(activity);
    }

    /**
     *  根据活动编号查询用户
     * @param registerId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Activity> findByRegisterId(String registerId) {
        return activityMapper.findByRegisterId(registerId);
    }

    /**
     *  据活动编号查询活动用户编号
     * @param registerId
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> findMatchNoByRegisterId(String registerId) {
        return activityMapper.findMatchNoByRegisterId(registerId);
    }

    /**
     *  根据字母查询未发送的用户
     * @param grouping
     * @return
     */
    @Transactional(readOnly = true)
    public List<Activity> findByMatchNoAndStatus(String grouping, String registerId) {
        return activityMapper.findByMatchNoAndStatus(grouping, registerId);
    }

    /**
     *  发送短信并修改状态
     * @param activityList
     * @return
     */
    public Integer updateStatus(List<Activity> activityList, Integer status) {
        Integer code = activityMapper.batchUpdateStatus(activityList, status);
        if (code == activityList.size()) {
            asyncService.asyncInvokeWithParameter(activityList);
        } else {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

    /**
     *  批量修改状态
     * @param list
     * @param status
     * @return
     */
    public Integer batchUpdateStatus(List<Activity> list, Integer status) {
        return activityMapper.batchUpdateStatus(list, status);
    }

    /**
     *  根据传入的活动编号、性别和理想性别查询
     * @param sex 性别
     * @param wantSex 理想性别
     * @return
     */
    @Transactional(readOnly = true)
    public List<Activity> findBySexAndWantSex(Integer sex, String wantSex, String registerId) {
        return activityMapper.findBySexAndWantSex(sex, wantSex, registerId);
    }

    /**
     *  更新活动人数少的男女一方编号(开启异步防止网页请求等待时间超时)
     *  @param registerId 活动编号
     *  @param type 类型，2代表更换cp
     * @return
     */
    @Async
    public Integer updateMatchNoByWoman(String registerId, Integer type) {
        Integer code = 0;
        String[] menAndWomenThan = activityMapper.findMamNumAndWomanNum(registerId).split(":");
        Activity activity1 = new Activity();
        activity1.setAtRegisterId(registerId);
        if (Integer.valueOf(menAndWomenThan[0]) > Integer.valueOf(menAndWomenThan[1])) {
            activity1.setSex(1);
        } else {
            activity1.setSex(0);
        }
        List<Activity> activityList = activityMapper.findActivityByRegisterIdAndSex(activity1);
        Integer groupNo = 1;//每个群的字母外数字
        Integer matchNoIndex = 0;//活动字母编号下标
        Integer matchNoSize = activityMapper.findCountByRegisterIdAndMatchNo(registerId, matchNo[matchNoIndex] + groupNo) + 1;//每个群的情侣数
        //获取每个群现阶段最大的情侣数字编号
        Integer maxMatch = activityMapper.findMatchNoMaxByRegisterIdAndMatchNo(registerId, matchNo[matchNoIndex] + groupNo);
        if (null == maxMatch) {
            maxMatch = 1;
        } else {
            maxMatch += maxMatch;
        }
        //每个群最多情侣对数
        Integer cpNo = Integer.valueOf(ParaConfController.paraConfController.findUrlByKey("ACTIVITY_CROWD_NUMBER"));
        List<CpFlow> flowList = new ArrayList<>();//cp流水
        List<CpRel> relList = new ArrayList<>();//cp关系
        Integer activitySize = activityList.size();//获取情侣数量
        Boolean fun = false;
        Integer groupNum = 0;
        Integer cpCopy = 0;//如果当前为更换cp，则替代cpNo
        //当情侣数组键的最后一个群少于10对并且是统一匹配时
        if (activitySize%cpNo < 10 && activitySize > cpNo && type == 1) {
            fun = true;
            groupNum = activitySize/cpNo;
        }
        for (Activity activity: activityList
             ) {
            if (fun && (groupNo - 1) * 26 + matchNoIndex + 1 > groupNum) {
                matchNoIndex = 0;
                groupNo = 1;
                maxMatch = activityMapper.findMatchNoMaxByRegisterIdAndMatchNo(registerId, matchNo[matchNoIndex] + groupNo) + 1;
            } else {
                //当前为跟换cp并且群组为A1群时，最大情侣数加上余数
                if (type == 2 && groupNo == 1 && matchNoIndex == 0 && groupNum > 0) {
                    cpCopy = cpNo + groupNum;
                } else {
                    cpCopy = cpNo;
                }
                if (matchNoSize > cpCopy) {
                    while (true) {
                        matchNoIndex++;
                        if (fun && (groupNo - 1) * 26 + matchNoIndex + 1 > groupNum) {
                            matchNoIndex = 0;
                            groupNo = 1;
                            maxMatch = activityMapper.findMatchNoMaxByRegisterIdAndMatchNo(registerId, matchNo[matchNoIndex] + groupNo) + 1;
                            break;
                        } else {
                            if (matchNoIndex <= 25) {
                                matchNoSize = activityMapper.findCountByRegisterIdAndMatchNo(registerId, matchNo[matchNoIndex] + groupNo) + 1;
                                if (matchNoSize < cpCopy) {
                                    maxMatch = activityMapper.findMatchNoMaxByRegisterIdAndMatchNo(registerId, matchNo[matchNoIndex] + groupNo);
                                    if (null == maxMatch) {
                                        maxMatch = 1;
                                    } else {
                                        maxMatch++;
                                    }
                                    break;
                                }
                            } else {
                                //26个字母都使用的情况下，增加数字，并将活动字母下标归0
                                groupNo++;
                                matchNoIndex = 0;
                            }
                        }
                    }
                }
            }
            String matchNos = matchNo[matchNoIndex] + groupNo;
            activity.setMatchNo(matchNos);
            activity.setCpNo(maxMatch);
            Activity manActivity = activityMapper.findActivityBySex(activity);
            manActivity.setCpNo(maxMatch);
            manActivity.setMatchNo(matchNos);
//            activityMapper.updateStatusBySex(activity);
            activityMapper.updateMatchNoAndStatusAndCpNoById(manActivity);
            CpRel cpRel = cpRelMapper.findByReqUserIdAndResUserId(manActivity.getAcUserId(), activity.getAcUserId());
            if (null != cpRel) {
                Date date = new Date();
                cpRel.setStatus("URS_CP_CONFIRM");
                cpRel.setCreateTime(date);
                cpRel.setSrcType("CST_WEEKCP");
                cpRel.setSrcId(activity.getAtRegisterId());
                code = cpRelMapper.updateByPrimaryKey(cpRel);
                if (code < 1)
                    throw new MyException(ResultEnums.ADDITION_FAILED);
            } else {
                cpRel = new CpRel(UUIDUtil.getEUUID(), manActivity.getAcUserId(), activity.getAcUserId(),"CST_WEEKCP", activity.getAtRegisterId(), "URS_CP_CONFIRM");
                relList.add(cpRel);
            }
            flowList.add(new CpFlow(UUIDUtil.getEUUID(), cpRel.getAcCpRelId(), manActivity.getAcUserId(), activity.getAcUserId(), "CST_WEEKCP", activity.getAtRegisterId(), "URS_CP_CONFIRM"));
            //将流水的插入时间改为当前时间，与统一更新的后一秒时间做区别，保证此处为最新数据
            flowList.get(flowList.size() - 1).preInsert();
            maxMatch++;
            matchNoSize++;
        }
        activityMapper.matchingBatchUpdate(activityList);
        if (relList.size() > 0) {
            code = cpRelMapper.insertByBatch(relList);
            if (code == relList.size()) {
                code = cpFlowMapper.insertByBatch(flowList);
                if (code != flowList.size()) {
                    System.out.println("流水插入失败");
                    code = 0;
                }
            } else {
                code = 0;
            }
        } else {
            code = cpFlowMapper.insertByBatch(flowList);
            if (code != flowList.size()) {
                code = 0;
            }
        }
        if (code == 0)
            throw new MyException(ResultEnums.ADDITION_FAILED);
        System.out.println("-----------匹配完成------------");
        return code;
    }

    /**
     *  匹配
     * @param result 排好情侣得用户集合
     * @param crowdNum 每个群的人数
     * @param type 类型，2代表更换cp
     * @return
     */
    public Integer sortMatching(List<Activity> result, Integer crowdNum, Integer type) {
//        if (type == 2)
//            updateStatusByConfirmCp(result.get(0).getAtRegisterId());
        Integer code = 0;
        Integer matchNoIndex = 0;
        Integer matchNoSize = activityMapper.findCountByRegisterIdAndMatchNo(result.get(0).getAtRegisterId(), matchNo[0]) + 1;
        Integer maxMatch = activityMapper.findMatchNoMaxByRegisterIdAndMatchNo(result.get(0).getAtRegisterId(), matchNo[0]);
//        List<Activity> activityList = new ArrayList<>();
        List<CpRel> cpRelList = new ArrayList<>();
        List<CpFlow> cpFlowList = new ArrayList<>();
        Integer activitySize = 0;
        for (Activity activity: result
        ) {
            //如果用户有cp，则结束匹配并将其踢出活动
            if (null != cpRelMapper.findRelByPhone(activity.getPhoneNumber())) {
                activityMapper.updateStatus(activity.getAtWkcpUserId(), -1);
                return -1;
            } else {
                while (crowdNum < matchNoSize/2) {
                    matchNoIndex++;
                    matchNoSize = activityMapper.findCountByRegisterIdAndMatchNo(result.get(0).getAtRegisterId(), matchNo[matchNoIndex]) + 1;
                    maxMatch = activityMapper.findMatchNoMaxByRegisterIdAndMatchNo(result.get(0).getAtRegisterId(), matchNo[0]);
                }
                if (activitySize%2 == 0) {
                    if (maxMatch == null)
                        maxMatch = 1;
                    maxMatch++;
                    String acCpRelId = UUIDUtil.getEUUID();
                    cpRelList.add(new CpRel(acCpRelId, activity.getAcUserId(), "CST_WEEKCP", activity.getAtRegisterId(), "URS_CP_CONFIRM"));
                    cpFlowList.add(new CpFlow(UUIDUtil.getEUUID(), acCpRelId, activity.getAcUserId(), "CST_WEEKCP", activity.getAtRegisterId(), "URS_CP_CONFIRM"));
                } else {
                    CpRel rel = cpRelList.get(cpRelList.size() - 1);
                    rel.setResUserId(activity.getAcUserId());
                    cpFlowList.get(cpFlowList.size() - 1).setResUserId(activity.getAcUserId());
                    CpRel cpRel = cpRelMapper.findByReqUserIdAndResUserId(rel.getReqUserId(), rel.getResUserId());
                    if (null != cpRel) {
                        Date date = new Date();
                        cpRel.setStatus("URS_CP_CONFIRM");
                        cpRel.setCreateTime(date);
                        cpRel.setSrcType("CST_WEEKCP");
                        cpRel.setSrcId(activity.getAtRegisterId());
                        code = cpRelMapper.updateByPrimaryKey(cpRel);
                        if (code < 1)
                            throw new MyException(ResultEnums.ADDITION_FAILED);
                        cpRelList.remove(cpRelList.size() - 1);
                        cpFlowList.get(cpFlowList.size() - 1).setAcCpRelId(cpRel.getAcCpRelId());
                    }
                }
                activity.setMatchNo(matchNo[matchNoIndex]);
                activity.setStatus(1);
                activity.setCpNo(maxMatch);
                matchNoSize++;
            }
            activitySize++;
        }
        code = activityMapper.matchingBatchUpdate(result);
        if (code == result.size()) {
            if (cpRelList.size() > 0) {
                code = cpRelMapper.insertByBatch(cpRelList);
                if (code == cpRelList.size()) {
                    code = cpFlowMapper.insertByBatch(cpFlowList);
                    if (code != cpFlowList.size()) {
                        code = 0;
                    }
                } else {
                    code = 0;
                }
            } else {
                code = cpFlowMapper.insertByBatch(cpFlowList);
                if (code != cpFlowList.size()) {
                    code = 0;
                }
            }
        } else {
            code = 0;
        }
        if (code == 0) {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

    /**
     *  更新编号及添加cp流水和关系
     * @param matchNo
     * @param activityList
     * @return
     */
    public Integer updateMatchNo(String matchNo, List<Activity> activityList, String atRegisterId) {
        for (Activity activity: activityList
             ) {
            if (null != cpRelMapper.findRelByPhone(activity.getPhoneNumber())) {
                activityMapper.updateStatus(activity.getAtWkcpUserId(), -1);
                return -1;
            }
        }
        //清空该活动cp关系
//        cpRelMapper.updateStatusByRegisterId(atRegisterId, "URS_CP_DELETED");
        Integer code = 0;
        //添加cp关系
        CpRel rel = new CpRel();
        rel.setSrcId(atRegisterId);
        rel.setSrcType("CST_WEEKCP");
        rel.setReqUserId(activityList.get(0).getAcUserId());
        rel.setResUserId(activityList.get(1).getAcUserId());
        CpRel cpRel = cpRelMapper.selectOne(rel);
        rel.setReqUserId(activityList.get(1).getAcUserId());
        rel.setResUserId(activityList.get(0).getAcUserId());
        CpRel cpRel1 = cpRelMapper.selectOne(rel);
        if (null != cpRel || null != cpRel1) {
            Date date = new Date();
            if (null != cpRel) {
                code = cpRelMapper.updateStatusByAcCpRelId(cpRel.getAcCpRelId(),"URS_CP_CONFIRM", date);
            } else if (null != cpRel1) {
                code = cpRelMapper.updateStatusByAcCpRelId(cpRel1.getAcCpRelId(),"URS_CP_CONFIRM", date);
            }
        } else {
            rel.setStatus("URS_CP_CONFIRM");
            rel.preInsert();
            code = cpRelMapper.insert(rel);
        }
        if (code > 0) {
            //添加cp关系流水
            CpFlow flow = new CpFlow();
            flow.setSrcId(atRegisterId);
            flow.setSrcType("CST_WEEKCP");
            //清空该活动cp流水
//            cpFlowMapper.delete(flow);
            if (null != cpRel) {
                flow.setAcCpRelId(cpRel.getAcCpRelId());
            } else if (null != cpRel1) {
                flow.setAcCpRelId(cpRel1.getAcCpRelId());
            } else {
                flow.setAcCpRelId(rel.getAcCpRelId());
            }
            flow.setStatus("URS_CP_CONFIRM");
            flow.setReqUserId(activityList.get(0).getAcUserId());
            flow.setResUserId(activityList.get(1).getAcUserId());
            flow.preInsert();
            code = cpFlowMapper.insert(flow);
            if (code > 0) {
                Activity activity = new Activity();
                activity.setMatchNo(matchNo);
                activity.setStatus(1);
                code = activityMapper.batchUpdate(activityList, activity);
            }
        }
        if (code == 0) {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

    /**
     *  更新除开活动情侣和有情侣的用户状态和cp编号和非cp的活动情侣状态
     * @param registerId
     * @return
     */
    public Integer updateByRegisterIdOrStatus(String registerId) {
        activityMapper.updateStatusByUserRel(registerId);
        activityMapper.updateByRegisterIdOrStatus(registerId);
        cpRelMapper.updateByRegisterIdOrStatus(registerId);
        return activityMapper.updateMatchNoByStatus(registerId);
    }

    /**
     * 根据活动将所有编号更新为空
     * @param registerId 活动编号
     * @return
     */
    public Integer updateMatchNoAll(String registerId) {
        //获取当前活动所有存在cp关系的数据
        List<CpRel> cpRelList = cpRelMapper.findByRegisterIdOrStatus(registerId);
        if (cpRelList.size() == 0) {
            return activityMapper.updateMatchNoAll(registerId);
        }
        List<CpFlow> cpFlowList = new ArrayList<>();
        for (CpRel cpRel: cpRelList
             ) {
            CpFlow cpFlow = new CpFlow(UUIDUtil.getEUUID(), cpRel.getAcCpRelId(),cpRel.getReqUserId(),
                    cpRel.getResUserId(),cpRel.getSrcType(),registerId,"URS_CP_DELETED");
            cpFlowList.add(cpFlow);
        }
        Integer code = cpFlowMapper.insertByBatch(cpFlowList);
        if (code > 0) {
            code =  cpRelMapper.batchUpdateStatus(cpRelList, "URS_CP_DELETED");
            if (code > 0) {
                code = activityMapper.updateMatchNoAll(registerId);
            }
        }
        if (code < 1) {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

    /**
     *  根据性别查找活动未排序用户
     * @param sex
     * @return
     */
    @Transactional(readOnly = true)
    public List<Activity> findMatchNoIsNullByMenOrWomen(Integer sex, String registerId) {
        return activityMapper.findMatchNoIsNullByMenOrWomen(sex, registerId);
    }

}
