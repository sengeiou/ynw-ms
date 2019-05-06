package com.ynw.system.service;

import com.ynw.system.dao.AcUserLabelMapper;
import com.ynw.system.entity.AcUserLabel;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AcUserLabelService extends MyService<AcUserLabelMapper, AcUserLabel> {

    /**
     *  条件分页查询数据
     * @param label
     * @param pageSize
     * @param start
     * @return
     */
    @Transactional(readOnly = true)
    public List<AcUserLabel> conditionQueryAcUserLabel(AcUserLabel label, Integer pageSize, Integer start) {
        return dao.conditionQueryAcUserLabel(label, pageSize, start);
    }

    /**
     *  条件统计数据数量
     * @param label
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(AcUserLabel label) {
        return dao.count(label);
    }

    /**
     *  将所有数据排序
     * @return
     */
    @Transactional(readOnly = true)
    public List<AcUserLabel> findBySortMax(String labelClassifyId) {
        return dao.findBySortMax(labelClassifyId);
    }

    /**
     *  查询用户所有标签
     * @return
     */
    @Transactional(readOnly = true)
    public List<AcUserLabel> findAllByUserId(String userId) {
        return dao.findAllByUserId(userId);
    }

    /**
     *  上移下移复用
     * @param labelId 移动对象编号
     * @param labelList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String labelId, List<AcUserLabel> labelList, Integer move) {
        Integer code = 0;
        Integer size = labelList.size();
        for (int i = 0; i < size; i++
        ) {
            if (labelList.get(i).getAcLabelId().equals(labelId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = labelList.get(i + move).getSort();
                    labelList.get(i + move).setSort(labelList.get(i).getSort());
                    code = this.update(labelList.get(i + move));
                    if (code > 0) {
                        labelList.get(i).setSort(sort);
                        code = this.update(labelList.get(i));
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
