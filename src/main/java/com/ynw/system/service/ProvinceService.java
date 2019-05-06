package com.ynw.system.service;

import com.ynw.system.dao.ProvinceMapper;
import com.ynw.system.entity.Province;
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
public class ProvinceService extends MyService<ProvinceMapper, Province> {

    /**
     *  获取降序数据
     * @return
     */
    public List<Province> findBySortMax() {
        return dao.findBySortMax();
    }

    /**
     *  根据排序获取所有数据
     * @return
     */
    public List<Province> conditionQueryProvince() {
        return dao.conditionQueryProvince();
    }

    /**
     *  上移下
     * @param bdProvinceId 移动对象编号
     * @param provinceList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String bdProvinceId, List<Province> provinceList, Integer move) {
        Integer code = 0;
        Integer size = provinceList.size();
        for (int i = 0; i < size; i++
        ) {
            if (provinceList.get(i).getBdProvinceId().equals(bdProvinceId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = provinceList.get(i + move).getSort();
                    provinceList.get(i + move).setSort(provinceList.get(i).getSort());
                    code = this.update(provinceList.get(i + move));
                    if (code > 0) {
                        provinceList.get(i).setSort(sort);
                        code = this.update(provinceList.get(i));
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
