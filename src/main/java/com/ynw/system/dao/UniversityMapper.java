package com.ynw.system.dao;
import com.ynw.system.entity.University;
import com.ynw.system.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UniversityMapper extends MyMapper<University> {

    List<University> conditionQueryUniversity(@Param("university") University university, @Param("start") Integer start,
                                        @Param("pageSize") Integer pageSize);

    Integer count(University university);

    University findById(String bdUniversityId);

}
