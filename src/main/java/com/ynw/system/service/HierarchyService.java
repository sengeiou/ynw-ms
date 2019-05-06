package com.ynw.system.service;

import com.ynw.system.dao.HierarchyMapper;
import com.ynw.system.entity.Hierarchy;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class HierarchyService extends MyService<HierarchyMapper, Hierarchy> {

    /**
     *  根据条件分页查询数据
     * @param hierarchy
     * @param pageSize
     * @param start
     * @return
     */
    public List<Hierarchy> conditionQueryHierarchy(Hierarchy hierarchy, Integer pageSize, Integer start) {
        return dao.conditionQueryHierarchy(hierarchy, pageSize, start);
    }

    /**
     *  据条件查询数据总数
     * @param hierarchy
     * @return
     */
    public Integer count(Hierarchy hierarchy) {
        return dao.count(hierarchy);
    }

    /**
     *  获取降序数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<Hierarchy> findByNoMax() {
        return dao.findByNoMax();
    }

    /**
     *  上移下移复用
     * @param acULevelId 移动对象编号
     * @param hierarchies 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String acULevelId, List<Hierarchy> hierarchies, Integer move) {
        Integer code = 0;
        Integer size = hierarchies.size();
        for (int i = 0; i < size; i++
        ) {
            if (hierarchies.get(i).getAcULevelId().equals(acULevelId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    // 因为等级唯一约束，先将位置腾出来
                    Integer sort = hierarchies.get(i + move).getNo();
                    hierarchies.get(i + move).setNo(-1);
                    code = update(hierarchies.get(i + move));
                    if (code > 0) {
                        hierarchies.get(i + move).setNo(hierarchies.get(i).getNo());
                        hierarchies.get(i).setNo(sort);
                        code = this.update(hierarchies.get(i));
                        if (code > 0)
                            code = update(hierarchies.get(i + move));
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
