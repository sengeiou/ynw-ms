package com.ynw.system.dao;

import com.ynw.system.entity.AcUser;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface AcUserMapper extends MyMapper<AcUser> {

    List<AcUser> conditionQueryAcUser(@Param("acUser") AcUser acUser, @Param("provinceId") String provinceId,
                                      @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer count(@Param("acUser") AcUser acUser, @Param("provinceId") String provinceId);

    AcUser findById(AcUser acUser);

    Integer update(AcUser acUser);

    Integer updateToke(@Param("expiryTime") Date expiryTime, @Param("acUserId") String acUserId);

    Integer nowCount(String nowTime);

    Integer replaceHeadById(String userId);

    Integer updateIDByUserId(AcUser acUser);

    List<AcUser> findUserIdAndUserNameByNo();

    List<AcUser> scheduleTimeRegisteredUser(String dateToString);

    List<AcUser> activeUser(String dateToString);

    /**
     *  获取最大的no编号对象
     * @return
     */
    AcUser findMaxNo();

    /**
     *  批量插入机器人
     * @param list
     * @return
     */
    Integer insertByBatch(List<AcUser> list);

    /**
     *  判断用户是否完成新人任务
     * @param acUserId
     * @return
     */
    boolean verifyPersonData(String acUserId);

}
