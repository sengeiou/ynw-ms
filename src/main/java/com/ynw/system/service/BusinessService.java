package com.ynw.system.service;

import com.ynw.system.dao.BusinessMapper;
import com.ynw.system.entity.Business;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/7
 */
@Service
@Transactional
public class BusinessService extends MyService<BusinessMapper, Business> {

    /**
     *  条件查询行业
     * @param business
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<Business> conditionQueryBusiness(Business business, Integer start, Integer pageSize) {
        return dao.conditionQueryBusiness(business, start, pageSize);
    }

    /**
     *  条件获取数据总数
     * @param business
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(Business business) {
        return dao.count(business);
    }

    /**
     *  获取降序数据
     * @return
     */
    @Transactional(readOnly = true)
    public List<Business> findBySortMax() {
        return dao.findBySortMax();
    }

//    /**
//     *  上移
//     * @param businessId 上移对象编号
//     * @param businessList 全体数据（降序）
//     * @return
//     */
//    public Integer moveUp(String businessId, List<Business> businessList) {
//        return move(businessId, businessList, 1);
//    }
//
//    /**
//     *  下移
//     * @param businessId 下移对象编号
//     * @param businessList 全体数据（降序）
//     * @return
//     */
//    public Integer moveDown(String businessId, List<Business> businessList) {
//        return move(businessId, businessList, -1);
//    }

    /**
     *  上移下移复用
     * @param businessId 移动对象编号
     * @param businessList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String businessId, List<Business> businessList, Integer move) {
        Integer code = 0;
        Integer size = businessList.size();
        for (int i = 0; i < size; i++
        ) {
            if (businessList.get(i).getBdBusinessId().equals(businessId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = businessList.get(i + move).getSort();
                    businessList.get(i + move).setSort(businessList.get(i).getSort());
                    code = this.update(businessList.get(i + move));
                    if (code > 0) {
                        businessList.get(i).setSort(sort);
                        code = this.update(businessList.get(i));
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
