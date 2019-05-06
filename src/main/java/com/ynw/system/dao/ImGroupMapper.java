package com.ynw.system.dao;

import com.ynw.system.entity.ImGroup;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ImGroupMapper extends MyMapper<ImGroup> {

    @Select("SELECT * FROM t_im_group WHERE `name` LIKE CONCAT('%',#{name},'%') AND business_type = #{businessType} " +
            "AND business_id = #{businessId} AND `status` = #{status}")
    List<ImGroup> findByInformation(ImGroup imGroup);

    List<ImGroup> findNowByRegisterId(ImGroup imGroup);

    List<ImGroup> findTest();

    List<String> findAllByRegisterId(ImGroup imGroup);


    ImGroup findByUserId(@Param("userId") String userId, @Param("registerId") String registerId, @Param("useType") Integer useType);

}
