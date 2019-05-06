package com.ynw.system.service;

import com.ynw.system.dao.DegreesMapper;
import com.ynw.system.entity.Degrees;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/8
 */
@Service
@Transactional
public class DegreesService extends MyService<DegreesMapper, Degrees> {

    /**
     *  条件查询学历
     * @param degrees
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<Degrees> conditionQueryDegrees(Degrees degrees, Integer start, Integer pageSize) {
        return dao.conditionQueryDegrees(degrees, start, pageSize);
    }

    /**
     *  条件获取数据总数
     * @param degrees
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Degrees degrees) {
        return dao.count(degrees);
    }

    /**
     *  获取降序数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<Degrees> findBySortMax() {
        return dao.findBySortMax();
    }

    /**
     *  上移下移
     * @param bdDegreesId 移动对象编号
     * @param degreesList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String bdDegreesId, List<Degrees> degreesList, Integer move) {
        Integer code = 0;
        Integer size = degreesList.size();
        for (int i = 0; i < size; i++
        ) {
            if (degreesList.get(i).getBdDegreesId().equals(bdDegreesId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = degreesList.get(i + move).getSort();
                    degreesList.get(i + move).setSort(degreesList.get(i).getSort());
                    code = this.update(degreesList.get(i + move));
                    if (code > 0) {
                        degreesList.get(i).setSort(sort);
                        code = this.update(degreesList.get(i));
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
