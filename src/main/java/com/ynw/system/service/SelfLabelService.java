package com.ynw.system.service;

import com.ynw.system.dao.InterestMapper;
import com.ynw.system.dao.SelfLabelMapper;
import com.ynw.system.entity.Interest;
import com.ynw.system.entity.SelfLabel;
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
public class SelfLabelService extends MyService<SelfLabelMapper, SelfLabel> {

    /**
     *  条件查询自我标签
     * @param selfLabel
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<SelfLabel> conditionQuerySelfLabel(SelfLabel selfLabel, Integer start, Integer pageSize) {
        return dao.conditionQuerySelfLabel(selfLabel, start, pageSize);
    }

    /**
     *  条件获取数据总数
     * @param selfLabel
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(SelfLabel selfLabel) {
        return dao.count(selfLabel);
    }

    /**
     *  获取降序数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<SelfLabel> findBySortMax() {
        return dao.findBySortMax();
    }

    /**
     *  上移下移复用
     * @param bdSelflabelId 移动对象编号
     * @param selfLabelList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String bdSelflabelId, List<SelfLabel> selfLabelList, Integer move) {
        Integer code = 0;
        Integer size = selfLabelList.size();
        for (int i = 0; i < size; i++
        ) {
            if (selfLabelList.get(i).getBdSelflabelId().equals(bdSelflabelId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = selfLabelList.get(i + move).getSort();
                    selfLabelList.get(i + move).setSort(selfLabelList.get(i).getSort());
                    code = this.update(selfLabelList.get(i + move));
                    if (code > 0) {
                        selfLabelList.get(i).setSort(sort);
                        code = this.update(selfLabelList.get(i));
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
     *  根据用户编号查询自我标签
     * @param acUserId 用户编号
     * @return
     */
    public List<SelfLabel> findSelfLabelByAcUserId(String acUserId) {
        return dao.findSelfLabelByAcUserId(acUserId);
    }

}
