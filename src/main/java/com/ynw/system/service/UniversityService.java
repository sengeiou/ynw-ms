package com.ynw.system.service;

import com.ynw.system.dao.UniversityMapper;
import com.ynw.system.entity.University;
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
public class UniversityService extends MyService<UniversityMapper, University> {

    /**
     *  条件查询高校
     * @param university
     * @param start
     * @param pageSize
     * @return
     */
    @Transactional(readOnly = true)
    public List<University> conditionQueryUniversity(University university, Integer start, Integer pageSize) {
        return dao.conditionQueryUniversity(university, start, pageSize);
    }

    /**
     *  条件获取数据总数
     * @param university
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(University university) {
        return dao.count(university);
    }

    /**
     *  根据编号查询高校
     * @param bdUniversityId 高校编号
     * @return
     */
    @Transactional(readOnly = true)
    public University findById(String bdUniversityId) {
        return dao.findById(bdUniversityId);
    }

}
