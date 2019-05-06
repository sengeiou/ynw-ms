package com.ynw.system.dao;

import com.ynw.system.entity.Activity;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityMapper extends MyMapper<Activity> {

    List<Activity> conditionQueryActivity(@Param("activity") Activity activity, @Param("pageSize") Integer pageSize,
                                          @Param("start") Integer start);

    Integer count(Activity activity);

    Activity findActivity(String activityId);

    List<Activity> findAll();

    List<Activity> findByMatchNoAndStatus(String grouping, String registerId);

    Integer updateStatus(@Param("atWkcpUserId") String atWkcpUserId, @Param("status") Integer status);

    Integer batchUpdateStatus(@Param("list") List<Activity> list, @Param("status") Integer status);

    Integer batchUpdate(@Param("list") List<Activity> list, @Param("activity") Activity activity);

    List<Activity> findBySexAndWantSex(@Param("sex") Integer sex, @Param("wantSex") String wantSex, @Param("registerId") String registerId);

    Integer updateMatchNoAll(String registerId);

    List<Activity> findMatchNoIsNullByMenOrWomen(@Param("sex") Integer sex, @Param("registerId") String registerId);

    List<Activity> findByRegisterId(String registerId);

    List<String> findMatchNoByRegisterId(String registerId);

    List<Activity> findByRegisterIdAndGroup(String registerId);

    List<Activity> findByRegisterIdAndLimit(String registerId);

    List<Activity> selectListLimit(@Param("activity") Activity activity, @Param("pageSize") Integer pageSize);

    Integer updateMatchNoByConfirmCp(String atRegisterId);

    Integer updateStatusByConfirmCp(String atRegisterId);

    /**
     *  批量更新匹配编号
     * @param list
     * @return
     */
    Integer matchingBatchUpdate(List<Activity> list);

    /**
     *  根据活动编号和匹配群字母查询数量(查女生)
     * @param registerId 活动编号
     * @param matchNo 匹配群字母
     * @return
     */
    Integer findCountByRegisterIdAndMatchNo(@Param("registerId") String registerId, @Param("matchNo") String matchNo);

    /**
     *  获取每个群的最大编号
     * @param registerId 活动编号
     * @param matchNo 匹配群字母
     * @return
     */
    Integer findMatchNoMaxByRegisterIdAndMatchNo(@Param("registerId") String registerId, @Param("matchNo") String matchNo);

    /**
     *  根据传入的对象更新cp的编号及状态
     * @param activity
     * @return
     */
    Integer updateStatusBySex(Activity activity);

    /**
     *  根据传入的对象查询cp的编号及状态
     * @param activity
     * @return
     */
    Activity findActivityBySex(Activity activity);

    /**
     *  根据传入的活动编号和性别查询未匹配数据
     * @param activity
     * @return
     */
    List<Activity> findActivityByRegisterIdAndSex(Activity activity);

    /**
     *  获得男女人数比
     * @param atRegisterId
     * @return
     */
    String findMamNumAndWomanNum(String atRegisterId);

    /**
     *  根据id更新数据
     * @param activity
     * @return
     */
    Integer updateMatchNoAndStatusAndCpNoById(Activity activity);

    /**
     *  更新除开活动情侣和有情侣的用户状态和cp编号
     * @param registerId
     * @return
     */
    Integer updateByRegisterIdOrStatus(String registerId);

    /**
     *  将当前活动下剔除活动的用户的编号等状态初始化
     * @param registerId
     * @return
     */
    Integer updateMatchNoByStatus(String registerId);

    /**
     *  将当前活动有情侣的用户剔除掉
     * @param registerId
     * @return
     */
    Integer updateStatusByUserRel(String registerId);

    /**
     *  根据用户编号更新当前活动的用户状态
     * @param list 用户编号集合
     * @param registerId 活动编号
     * @param status 状态
     * @return
     */
    Integer updateStatusByUserId(@Param("list") List<String> list, @Param("registerId") String registerId, @Param("status") Integer status);

}
