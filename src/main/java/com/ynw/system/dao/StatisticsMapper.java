package com.ynw.system.dao;

import org.apache.ibatis.annotations.Select;

public interface StatisticsMapper {

    @Select("SELECT COUNT(ac_user_id) FROM t_ac_user WHERE `type` = 0")
    Integer acUserCount();

    @Select("SELECT COUNT(ac_user_id) FROM t_ac_user WHERE create_time LIKE CONCAT('%',#{nowTime},'%') AND `type` = 0")
    Integer nowCount(String nowTime);

    @Select("SELECT COUNT(ac_attention_rel_id) FROM t_ac_attention_rel WHERE create_time LIKE CONCAT('%',#{nowTime},'%') AND status='URS_ADD_ATTENTION'")
    Integer nowAttention(String nowTime);

    @Select("SELECT COUNT(ac_friend_rel_id) FROM t_ac_friend_rel WHERE create_time LIKE CONCAT('%',#{nowTime},'%') AND status='URS_ACC_FRIEND_REQ'")
    Integer nowFriend(String nowTime);

    @Select("SELECT COUNT(tk_signin_flow_id) FROM t_tk_signin_flow WHERE create_time LIKE CONCAT('%',#{nowTime},'%')")
    Integer nowSignIn(String nowTime);

    @Select("SELECT COUNT(ds_mood_id) FROM t_ds_mood WHERE create_time LIKE CONCAT('%',#{nowTime},'%') AND (root_mood_id IS NULL OR is_transpond=1)")
    Integer nowMood(String nowTime);

    @Select("SELECT SUM(quantity) FROM t_ac_bean_flow WHERE create_time LIKE CONCAT('%',#{nowTime},'%') " +
            "AND type=1 AND asso_business_key!='BBT_MOOD_REWARD'")
    Integer nowLoveGrant(String nowTime);

    @Select("SELECT SUM(quantity) FROM t_ac_bean_flow WHERE create_time LIKE CONCAT('%',#{nowTime},'%') " +
            "AND type=0 AND asso_business_key!='BBT_REWARD_MOOD'")
    Integer nowLoveRecycle(String nowTime);

    @Select("SELECT SUM(quantity) FROM t_ac_bean_flow WHERE create_time LIKE CONCAT('%',#{nowTime},'%') " +
            "AND (asso_business_key='BBT_REWARD_MOOD' OR asso_business_key='BBT_ACCEPT_FRIEND_NOTICE')")
    Integer nowLoveTransfer(String nowTime);

    @Select("SELECT COUNT(kb_exampaper_test_id) FROM t_kb_exampaper_test WHERE create_time LIKE CONCAT('%',#{nowTime},'%')")
    Integer nowExamText(String nowTime);

    @Select("SELECT COUNT(DISTINCT ac_user_id) FROM t_kb_exampaper_test WHERE create_time LIKE CONCAT('%',#{nowTime},'%')")
    Integer nowExamUserNum(String nowTime);

}
