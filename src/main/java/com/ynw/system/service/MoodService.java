package com.ynw.system.service;

import com.ynw.system.dao.MoodMapper;
import com.ynw.system.entity.Mood;
import com.ynw.system.entity.PushBody;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/14
 */
@Service
@Transactional
public class MoodService {

    @Autowired
    private MoodMapper moodMapper;
    @Autowired
    private PushService pushService;

    /**
     *  条件查询问卷测试
     * @param mood
     * @param label 标签编号
     * @return
     */
    @Transactional(readOnly = true)
    public List<Mood> conditionQueryMood(Mood mood, String label, Integer start, Integer pageSize) {
        return moodMapper.conditionQueryMood(mood, label, start, pageSize);
    }

    /**
     * 条件查询数据总数
     * @param mood
     * @param label 标签编号
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Mood mood, String label) {
        return moodMapper.count(mood, label);
    }

    /**
     *  根据编号查询单个数据
     * @param dsMoodId 编号
     * @return
     */
    @Transactional(readOnly = true)
    public Mood findById(String dsMoodId) {
        return moodMapper.findById(dsMoodId);
    }

    /**
     *  根据根目录查询评论和转发
     * @param rootMoodId
     * @return
     */
    @Transactional(readOnly = true)
    public List<Mood> findMoodCommentByRootMoodId(String rootMoodId) {
        return moodMapper.findMoodCommentByRootMoodId(rootMoodId);
    }

    /**
     *  查询当天发布动态数
     * @param nowTime
     * @return
     */
    public Integer nowCount(String nowTime) {
        return moodMapper.nowCount(nowTime);
    }

    /**
     *  逻辑删除
     * @param moodId
     * @param content
     * @return
     */
    public Integer delete(String moodId, String content) {
        Integer code = moodMapper.delete(moodId);
        if (code > 0 && StringUtils.isNotEmpty(content)) {
            Mood mood = moodMapper.findById(moodId);
            if (null == mood)
                throw new MyException(ResultEnums.NO_DATA_ERROR);
            PushBody body = new PushBody("PMT_TEXT","PMG_SYSTEM","PMS_SINGLE","话题发布失败",content);
            pushService.insertMgBody(null, null, true, body, "PMBT_PLATFORM_TO_REMIND", mood.getAcUserId());
        }
        return moodMapper.delete(moodId);
    }

}
