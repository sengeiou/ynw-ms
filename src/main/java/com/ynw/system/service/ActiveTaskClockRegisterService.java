package com.ynw.system.service;

import com.ynw.system.dao.ActiveTaskClockRegisterMapper;
import com.ynw.system.entity.ActiveTaskClockRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ActiveTaskClockRegisterService {

    @Autowired
    private ActiveTaskClockRegisterMapper registerMapper;

    /**
     *  根据条件分页查询数据
     * @param register
     * @param pageSize
     * @param start
     * @return
     */
    @Transactional(readOnly = true)
    public List<ActiveTaskClockRegister> conditionQueryActiveTaskClockRegister(ActiveTaskClockRegister register, Integer pageSize, Integer start) {
        return registerMapper.conditionQueryActiveTaskClockRegister(register, pageSize, start);
    }

    /**
     *  根据条件查找数据总数
     * @param register
     * @return
     */
    @Transactional(readOnly = true)
    public Integer count(ActiveTaskClockRegister register) {
        return registerMapper.count(register);
    }

}
