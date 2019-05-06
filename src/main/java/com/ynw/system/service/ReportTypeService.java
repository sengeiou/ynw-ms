package com.ynw.system.service;

import com.ynw.system.dao.ReportTypeMapper;
import com.ynw.system.entity.ReportType;
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
public class ReportTypeService extends MyService<ReportTypeMapper, ReportType> {

    /**
     *  查询所有
     * @return
     */
    public List<ReportType> findAll() {
        return dao.findAll();
    }

    /**
     *  获取降序数据
     * @return
     */
    public List<ReportType> findBySortMax() {
        return dao.findBySortMax();
    }

    /**
     *  上移下移复用
     * @param syReportCtgyId 移动对象编号
     * @param reportTypeList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String syReportCtgyId, List<ReportType> reportTypeList, Integer move) {
        Integer code = 0;
        Integer size = reportTypeList.size();
        for (int i = 0; i < size; i++
        ) {
            if (reportTypeList.get(i).getSyReportCtgyId().equals(syReportCtgyId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = reportTypeList.get(i + move).getSort();
                    reportTypeList.get(i + move).setSort(reportTypeList.get(i).getSort());
                    code = this.update(reportTypeList.get(i + move));
                    if (code > 0) {
                        reportTypeList.get(i).setSort(sort);
                        code = this.update(reportTypeList.get(i));
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
