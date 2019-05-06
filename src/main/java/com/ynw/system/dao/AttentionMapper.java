package com.ynw.system.dao;

import com.ynw.system.entity.AcUser;
import com.ynw.system.entity.Attention;
import com.ynw.system.entity.Friend;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AttentionMapper {

    List<Attention> conditionQueryAttention(@Param("acUser") AcUser acUser, @Param("provinceId") String provinceId,
                                            @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer count(@Param("acUser") AcUser acUser, @Param("provinceId") String provinceId);

    Integer nowCount(String nowTime);

}
