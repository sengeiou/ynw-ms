package com.ynw.system.dao;

import com.ynw.system.entity.InviteTask;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InviteTaskMapper extends MyMapper<InviteTask> {

    /**
     *  条件查询数据
     * @param inviteTask
     * @param pageSize
     * @param start
     * @return
     */
    List<InviteTask> conditionQueryInviteTask(@Param("inviteTask") InviteTask inviteTask, @Param("pageSize") Integer pageSize,
                                              @Param("start") Integer start);

    /**
     *  条件查询数据总数
     * @param inviteTask
     * @return
     */
    Integer count(InviteTask inviteTask);

    int completeTask(@Param("acUserId") String acUserId, @Param("taskName")String taskName);

    Boolean isComplete(String acUserId);

}
