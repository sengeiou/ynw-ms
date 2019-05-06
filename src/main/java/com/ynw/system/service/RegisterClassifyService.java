package com.ynw.system.service;

import com.ynw.system.dao.RegisterClassifyMapper;
import com.ynw.system.entity.RegisterClassify;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ChengZhi
 * @version 2019/1/19
 */
@Service
@Transactional
public class RegisterClassifyService extends MyService<RegisterClassifyMapper, RegisterClassify> {

    /**
     *  根据时间排序全部数据
     * @return
     */
    public List<RegisterClassify> conditionQueryRegisterClassify() {
        return dao.conditionQueryRegisterClassify();
    }

}
