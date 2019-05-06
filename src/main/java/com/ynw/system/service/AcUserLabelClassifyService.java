package com.ynw.system.service;

import com.ynw.system.dao.AcUserLabelClassifyMapper;
import com.ynw.system.entity.AcUserLabelClassify;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AcUserLabelClassifyService extends MyService<AcUserLabelClassifyMapper, AcUserLabelClassify> {

    /**
     *  获取正排序后数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<AcUserLabelClassify> findAll() {
        return dao.findAll();
    }

    /**
     *  获取倒序数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<AcUserLabelClassify> findBySortMax() {
        return dao.findBySortMax();
    }

    /**
     *  上移下移复用
     * @param labelClassifyId 移动对象编号
     * @param labelClassifyList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String labelClassifyId, List<AcUserLabelClassify> labelClassifyList, Integer move) {
        Integer code = 0;
        Integer size = labelClassifyList.size();
        for (int i = 0; i < size; i++
        ) {
            if (labelClassifyList.get(i).getAcLabelCtgyId().equals(labelClassifyId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = labelClassifyList.get(i + move).getSort();
                    labelClassifyList.get(i + move).setSort(labelClassifyList.get(i).getSort());
                    code = this.update(labelClassifyList.get(i + move));
                    if (code > 0) {
                        labelClassifyList.get(i).setSort(sort);
                        code = this.update(labelClassifyList.get(i));
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
