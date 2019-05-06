package com.ynw.system.service;

import com.ynw.system.dao.RegisterMapper;
import com.ynw.system.entity.Register;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ChengZhi
 * @version 2019/1/19
 */
@Service
@Transactional
public class RegisterService extends MyService<RegisterMapper, Register> {

    /**
     *  条件查询数据
     * @param register
     * @param pageSize
     * @param start
     * @return
     */
    public List<Register> conditionQueryRegister(Register register, Integer pageSize, Integer start, Integer status) {
        return dao.conditionQueryRegister(register, pageSize, start, status);
    }

    /**
     *  获取查询总数
     * @param register
     * @return
     */
    public Integer count(Register register, Integer status) {
        return dao.count(register, status);
    }

}
