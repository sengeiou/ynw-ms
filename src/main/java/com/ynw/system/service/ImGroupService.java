package com.ynw.system.service;

import com.ynw.system.dao.ImGroupMapper;
import com.ynw.system.entity.ImGroup;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImGroupService extends MyService<ImGroupMapper, ImGroup> {

    /**
     *  根据条件查询（聊天群名字模糊查询）
     * @param imGroup
     * @return
     */
    public List<ImGroup> findByInformation(ImGroup imGroup) {
        return dao.findByInformation(imGroup);
    }

    /**
     *  根据条件查询
     * @param group
     * @return
     */
    public List<ImGroup> findNowByRegisterId(ImGroup group) {
        return dao.findNowByRegisterId(group);
    }

    public List<String> findAllByRegisterId(ImGroup imGroup) {
        return dao.findAllByRegisterId(imGroup);
    }

}
