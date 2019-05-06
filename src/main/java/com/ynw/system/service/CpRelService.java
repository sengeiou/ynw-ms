package com.ynw.system.service;

import com.ynw.system.dao.CpFlowMapper;
import com.ynw.system.dao.CpRelMapper;
import com.ynw.system.dao.ImGroupMapper;
import com.ynw.system.entity.CpFlow;
import com.ynw.system.entity.CpRel;
import com.ynw.system.entity.ImGroup;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import com.ynw.system.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CpRelService extends MyService<CpRelMapper, CpRel> {

    @Resource
    private CpFlowMapper cpFlowMapper;
    @Resource
    private ImGroupMapper imGroupMapper;
    @Autowired
    private HuanXinService huanXinService;

    public Integer delete(String registerId) {
        List<CpRel> cpRel = this.selectList(new CpRel(registerId, "URS_CP_WAIT_RELIEVE"));
        List<CpFlow> cpFlowList = new ArrayList<>();
        List<ImGroup> imGroupList = new ArrayList<>();
        for (CpRel rel: cpRel
        ) {
            cpFlowList.add(new CpFlow(UUIDUtil.getEUUID(), rel.getAcCpRelId(), rel.getReqUserId(),
                    rel.getResUserId(), rel.getSrcType(), rel.getSrcId(), "URS_CP_DELETED"));
            imGroupList.add(imGroupMapper.findByUserId(rel.getReqUserId(),registerId, 1));
        }
        Integer code = dao.batchUpdateStatus(cpRel, "URS_CP_DELETED");
        if (code == cpRel.size()) {
            code = cpFlowMapper.insertByBatch(cpFlowList);
            if (code == cpFlowList.size()) {
                deleteImGroup(imGroupList);
            } else {
                code = 0;
            }
        } else {
            code = 0;
        }
        if (code == 0) {
            throw new MyException(ResultEnums.HUAN_XIN_GROUP_DISSOLVE_ERROR);
        }
        return code;
    }

    public void deleteImGroup(List<ImGroup> groupList) {
        for (ImGroup group: groupList
        ) {
            if (!huanXinService.deleteGroup(group.getHxGroupId()))
                throw new MyException(ResultEnums.HUAN_XIN_GROUP_DISSOLVE_ERROR);
        }
    }

}
