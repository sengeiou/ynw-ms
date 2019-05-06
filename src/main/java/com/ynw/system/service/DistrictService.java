package com.ynw.system.service;

import com.ynw.system.dao.DistrictMapper;
import com.ynw.system.entity.District;
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
public class DistrictService extends MyService<DistrictMapper, District> {

    /**
     * 根据城市查询县区
     * @param district
     * @return
     */
    public List<District> conditionQueryDistrict(District district) {
        return dao.conditionQueryDistrict(district);
    }

    /**
     *  根据城市获取降序数据
     * @return
     */
    public List<District> findBySortMax(District district) {
        return dao.findBySortMax(district);
    }

    /**
     *  上移下移复用
     * @param bdDistrictId 移动对象编号
     * @param districtList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String bdDistrictId, List<District> districtList, Integer move) {
        Integer code = 0;
        Integer size = districtList.size();
        for (int i = 0; i < size; i++
        ) {
            if (districtList.get(i).getBdDistrictId().equals(bdDistrictId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = districtList.get(i + move).getSort();
                    districtList.get(i + move).setSort(districtList.get(i).getSort());
                    code = this.update(districtList.get(i + move));
                    if (code > 0) {
                        districtList.get(i).setSort(sort);
                        code = this.update(districtList.get(i));
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
