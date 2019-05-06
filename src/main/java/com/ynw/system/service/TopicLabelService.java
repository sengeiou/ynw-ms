package com.ynw.system.service;

import com.ynw.system.dao.TopicLabelMapper;
import com.ynw.system.entity.TopicLabel;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class TopicLabelService {

    @Resource
    private TopicLabelMapper topicLabelMapper;

    /**
     *  更新
     * @param topicLabel
     * @return
     */
    public Integer updateLabel(TopicLabel topicLabel) {
        return topicLabelMapper.update(topicLabel);
    }

    /**
     *  添加话题标签
     * @param topicLabel
     * @return
     */
    public Integer insert(TopicLabel topicLabel) {
        topicLabel.preInsert();
        return topicLabelMapper.insert(topicLabel);
    }

    /**
     *  删除话题标签
     * @param topicLabel
     * @return
     */
    public Integer delete(TopicLabel topicLabel) {
        return topicLabelMapper.delete(topicLabel);
    }

    /**
     *  更新话题标签
     * @param topicLabel
     * @return
     */
    public Integer update(TopicLabel topicLabel) {
        return topicLabelMapper.updateByPrimaryKey(topicLabel);
    }

    /**
     *  查询单个话题标签
     * @param topicLabel
     * @return
     */
    @Transactional(readOnly = true)
    public TopicLabel findOne(TopicLabel topicLabel) {
        return topicLabelMapper.selectOne(topicLabel);
    }

    /**
     *  查询所有话题标签
     * @return
     */
    @Transactional(readOnly = true)
    public List<TopicLabel> findAll() {
        return topicLabelMapper.findAll();
    }

    /**
     *  获取降序数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<TopicLabel> findBySortMax() {
        return topicLabelMapper.findBySortMax();
    }

    /**
     *  上移下移
     * @param dsLabelId 移动对象编号
     * @param topicLabelList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String dsLabelId, List<TopicLabel> topicLabelList, Integer move) {
        Integer code = 0;
        Integer size = topicLabelList.size();
        for (int i = 0; i < size; i++
        ) {
            if (topicLabelList.get(i).getDsLabelId().equals(dsLabelId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = topicLabelList.get(i + move).getSort();
                    topicLabelList.get(i + move).setSort(topicLabelList.get(i).getSort());
                    code = this.update(topicLabelList.get(i + move));
                    if (code > 0) {
                        topicLabelList.get(i).setSort(sort);
                        code = this.update(topicLabelList.get(i));
                    }
                }
                break;
            }
        }
        if (code < 1) {
            //抛出一个错误保证回滚
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

}
