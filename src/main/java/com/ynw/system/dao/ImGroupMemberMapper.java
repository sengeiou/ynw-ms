package com.ynw.system.dao;

import com.ynw.system.entity.ImGroupMember;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImGroupMemberMapper extends MyMapper<ImGroupMember> {

    Integer updateBlackStatus(@Param("status")Integer status, @Param("members") List<String> members, @Param("groupId") String groupId);

    Integer deleteMemeberInGroup(@Param("members") String[] members,@Param("groupId") String groupId);

    Integer insertMember(ImGroupMember imGroupMember);

}
