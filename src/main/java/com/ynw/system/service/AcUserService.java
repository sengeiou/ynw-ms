package com.ynw.system.service;

import com.ynw.system.dao.*;
import com.ynw.system.entity.*;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import com.ynw.system.util.DateUtils;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import com.ynw.system.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 *  @author ChengZhi
 *  @version 2018-12/11
 */
@Service
@Transactional
public class AcUserService {

    @Autowired
    private AcUserMapper acUserMapper;
    @Autowired
    private PushService pushService;
    @Autowired
    private HuanXinService huanXinService;
    @Resource
    private NotifyMapper notifyMapper;
    @Resource
    private PrivacyMapper privacyMapper;
    @Resource
    private InviteTaskMapper inviteTaskMapper;
    @Resource
    private SweetsFlowMapper sweetsFlowMapper;
    @Resource
    private SweetsSumMapper sweetsSumMapper;
    @Autowired
    private AcUserImageService acUserImageService;
    private final static  String PERFECT_PERSON_DATA="PERFECT_PERSON_DATA";

    /**
     *  照片审核
     * @param acUserImage
     * @param content
     * @return
     */
    public ResponseEntity<Result> updateAcUserImageById(AcUserImage acUserImage, String content) {
        AcUserImage image = new AcUserImage();
        image.setAcUImageId(acUserImage.getAcUImageId());
        List<AcUserImage> userImage = acUserImageService.selectList(image);
        if (userImage.size() == 1) {
            if (acUserImage.getStatus() == -1 && userImage.get(0).getIsCover() == 1) {
                AcUserImage image1 = new AcUserImage();
                image1.setAcUserId(userImage.get(0).getAcUserId());
                image1.setIsCover(1);
                //查询是否存在封面，存在就离开
                AcUserImage image2 = acUserImageService.selectOne(image1);
                if (null == image2 || image2.getAcUImageId().equals(userImage.get(0).getAcUImageId())) {
                    if (image2.getAcUImageId().equals(userImage.get(0).getAcUImageId())) {
                        image2.setIsCover(0);
                        if(acUserImageService.updateAcUserImageById(image2, content) < 1) {
                            return ResultUtil.errorResponse(ResultEnums.IS_COVER_ERROR);
                        }
                        //修改封面成功，改变userImage封面状态
                        userImage.get(0).setIsCover(0);
                    }
                    image1.setIsCover(null);
                    image1.setStatus(1);
                    //查询是否存在审核通过的照片
                    List<AcUserImage> imageList = acUserImageService.selectList(image1);
                    if (imageList.size() > 0) {
                        imageList.get(0).setIsCover(1);
                        if (acUserImageService.updateAcUserImageById(imageList.get(0), content) < 1) {
                            return ResultUtil.errorResponse(ResultEnums.IS_COVER_ERROR);
                        }
                    } else {
                        //查询是否存在未审核的照片
                        image1.setStatus(0);
                        List<AcUserImage> userImageList = acUserImageService.selectList(image1);
                        if (userImageList.size() > 0) {
                            userImageList.get(0).setIsCover(1);
                            if (acUserImageService.updateAcUserImageById(userImageList.get(0), content) < 1) {
                                return ResultUtil.errorResponse(ResultEnums.IS_COVER_ERROR);
                            }
                        }
                    }
                }
            }
            userImage.get(0).setStatus(acUserImage.getStatus());
            if (acUserImageService.updateAcUserImageById(userImage.get(0), content) > 0) {
                String userId = userImage.get(0).getAcUserId();
                if (verifyPersonData(userId)) {
                    AcUser user = selectOne(new AcUser(userId));
                    completeTask(user, PERFECT_PERSON_DATA);
                }
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_OR_MULTI);
    }



    /**
     * 判断是否完成新人任务
     * @param acUserId
     * @return
     */
    private Boolean verifyPersonData(String acUserId){
        return acUserMapper.verifyPersonData(acUserId);
    }

    /**
     *  完成任务给与糖果奖励
     * @param acUser
     * @param taskName
     */
    public void completeTask(AcUser acUser,String taskName){
        String acUserId=acUser.getAcUserId();
        InviteTask inviteUserTask = new InviteTask();
        inviteUserTask.preInserts(acUserId,taskName);
        //如果该用户已经领取任务则 把任务设置为已经完成
        if(inviteTaskMapper.selectCount(inviteUserTask)!=0) {
            inviteTaskMapper.completeTask(acUserId,taskName);
        }
        //如果该用户完成了所有给他的新人任务,则给邀请他的人进行糖果奖励
        if (inviteTaskMapper.isComplete(acUserId)) {
            SweetsFlow sweetsFlow=new SweetsFlow();
            sweetsFlow.preInserts(acUser.getReferrerId(),acUserId);
            optionSugar(sweetsFlow);
        }
    }

    /**
     *  添加糖果总量和流水
     * @param sweetsFlow
     */
    public void optionSugar(SweetsFlow sweetsFlow){
        sweetsFlowMapper.insert(sweetsFlow);
        SweetsSum sweetsSum= sweetsSumMapper.selectOne(new SweetsSum(sweetsFlow.getAcUserId()));
        if(Objects.isNull(sweetsSum)){
            if (sweetsFlow.getType()==1){
                throw new MyException(ResultEnums.ILLEGAL_OPERATION);
            }
            sweetsSum = new SweetsSum(sweetsFlow.getAcUserId(),sweetsFlow.getQuantity());
            sweetsSumMapper.insert(sweetsSum);
        }else{
            if (sweetsFlow.getType()==1) {
                sweetsSum.setSum(sweetsSum.getSum() - sweetsFlow.getQuantity());
            }else{
                sweetsSum.setSum(sweetsSum.getSum() + sweetsFlow.getQuantity());
            }
            if (sweetsSum.getSum()<0){
                throw new MyException(ResultEnums.ILLEGAL_OPERATION);
            }
            sweetsSumMapper.updateByPrimaryKeySelective(sweetsSum);
        }
    }

    /**
     * 用于注册im用户的方法，传入一个AcUser对象
     * @param user
     * @return
     */
    public Boolean register(AcUser user) {
        //注册环信用户
        huanXinService.insertIm(user.getPhoneNumber());
        Notify acNotify = new Notify();
        acNotify.setAcUserId(user.getAcUserId());
        acNotify.preInsert();
        Privacy acPrivacy = new Privacy();
        acPrivacy.setAcUserId(user.getAcUserId());
        acPrivacy.preInsert();
        //给用户默认开启各种通知的推送
        notifyMapper.insert(acNotify);
        //给用户开启默认隐私
        privacyMapper.insert(acPrivacy);
        return true;
    }

    /**
     *  批量插入机器人
     * @param list
     * @return
     */
    public Integer insertByBatch(List<AcUser> list) {
        return acUserMapper.insertByBatch(list);
    }

    /**
     *  获取最大的no编号对象
     * @return
     */
    public AcUser findMaxNo() {
        return acUserMapper.findMaxNo();
    }

    /**
     *  根据传入时间字符串模糊查询数据
     * @param dateToString
     * @return
     */
    public List<AcUser> scheduleTimeRegisteredUser(String dateToString) {
        return acUserMapper.scheduleTimeRegisteredUser(dateToString);
    }

    /**
     *  根据传入时间字符串模糊查询当前日期活跃用户数据
     * @param dateToString
     * @return
     */
    public List<AcUser> activeUser(String dateToString) {
        return acUserMapper.activeUser(dateToString);
    }

    /**
     *  条件查询用户
     * @param acUser
     * @param provinceId 省份编号
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<AcUser> conditionQueryAcUser(AcUser acUser,String provinceId, Integer start, Integer pageSize) {
        return acUserMapper.conditionQueryAcUser(acUser, provinceId, start, pageSize);
    }

    /**
     *  条件查询数据总数
     * @param acUser
     * @param provinceId
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(AcUser acUser, String provinceId) {
        return acUserMapper.count(acUser, provinceId);
    }

    /**
     *  根据编号查询用户
     * @param acUser
     * @return
     */
    @Transactional(readOnly = true)
    public AcUser findById(AcUser acUser) {
        return acUserMapper.findById(acUser);
    }

    /**
     *  根据条件查询用户
     * @param acUser
     * @return
     */
    @Transactional(readOnly = true)
    public AcUser selectOne(AcUser acUser) {
        return acUserMapper.selectOne(acUser);
    }

    /**
     *  根据条件查询用户
     * @param acUser
     * @return
     */
    @Transactional(readOnly = true)
    public List<AcUser> selectList(AcUser acUser) {
        return acUserMapper.select(acUser);
    }

    /**
     *  修改用户状态
     * @param acUser
     * @return
     */
    public Integer update(AcUser acUser) {
        Integer code = acUserMapper.updateToke(DateUtils.getBeginDayOfYesterday(),acUser.getAcUserId());
        if (code > 0) {
            code = acUserMapper.update(acUser);
        }
        if (code <1) {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

    /**
     *  查询当天注册用户
     * @param nowTime
     * @return
     */
    @Transactional(readOnly = true)
    public Integer nowCount(String nowTime) {
        return acUserMapper.nowCount(nowTime);
    }

    /**
     *  根据编号将头像替换成系统头像
     * @param userId
     * @return
     */
    public Integer replaceHeadById(String userId) {
        return acUserMapper.replaceHeadById(userId);
    }

    /**
     *  修改用户身份证审核状态
     * @param user
     * @param content 审核不通过原因
     * @return
     */
    public Integer updateIDByUserId(AcUser user, String content) {
        Integer code = acUserMapper.updateIDByUserId(user);
        if (code > 0 && StringUtils.isNotEmpty(content)) {
            PushBody body = new PushBody("PMT_TEXT","PMG_SYSTEM","PMS_SINGLE","身份证认证失败",
                    "您的身份证审核不通过（"+content+"),请从新选择符合的身份证件照片！！！");
            pushService.insertMgBody(null, null, true, body, "PMBT_PLATFORM_TO_REMIND", user.getAcUserId());
        }
        return code;
    }

    /**
     *  根据字典查询系统用户id和昵称
     * @return
     */
    @Transactional(readOnly = true)
    public List<AcUser> findUserIdAndUserNameByNo() {
        return acUserMapper.findUserIdAndUserNameByNo();
    }

    /**
     *  根据id修改数据
     * @param acUser
     * @return
     */
    public Integer updateByPrimaryKey(AcUser acUser) {
        return acUserMapper.updateByPrimaryKey(acUser);
    }

}
