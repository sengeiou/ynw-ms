package com.ynw.system.dao;

import com.ynw.system.entity.Mood;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MoodMapper {

    List<Mood> conditionQueryMood(@Param("mood") Mood mood, @Param("label") String label,
                                           @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer count(@Param("mood") Mood mood, @Param("label") String label);

    Mood findById(String dsMoodId);

    List<Mood> findMoodCommentByRootMoodId(String rootMoodId);

    Integer nowCount(String nowTime);

    Integer delete(String moodId);

}
