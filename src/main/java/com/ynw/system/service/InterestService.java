package com.ynw.system.service;

import com.ynw.system.dao.InterestMapper;
import com.ynw.system.entity.Interest;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/10
 */
@Service
@Transactional
public class InterestService extends MyService<InterestMapper, Interest> {

    /**
     *  条件查询兴趣标签
     * @param interest
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<Interest> conditionQueryInterest(Interest interest, Integer start, Integer pageSize) {
        return dao.conditionQueryInterest(interest, start, pageSize);
    }

    /**
     *  条件获取数据总数
     * @param interest
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Interest interest) {
        return dao.count(interest);
    }

    /**
     *  获取降序数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<Interest> findBySortMax() {
        return dao.findBySortMax();
    }

    /**
     *  上移下移复用
     * @param bdInterestId 移动对象编号
     * @param interests 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String bdInterestId, List<Interest> interests, Integer move) {
        Integer code = 0;
        Integer size = interests.size();
        for (int i = 0; i < size; i++
        ) {
            if (interests.get(i).getBdInterestId().equals(bdInterestId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = interests.get(i + move).getSort();
                    interests.get(i + move).setSort(interests.get(i).getSort());
                    code = this.update(interests.get(i + move));
                    if (code > 0) {
                        interests.get(i).setSort(sort);
                        code = this.update(interests.get(i));
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

    /**
     *  根据用户编号查询兴趣标签
     * @param acUserId 用户编号
     * @return
     */
    public List<Interest> findInterestByAcUserId(String acUserId) {
        return dao.findInterestByAcUserId(acUserId);
    }

}
