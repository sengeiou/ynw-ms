package com.ynw.system.service;

import com.ynw.system.dao.ResourceAndRoleMapper;
import com.ynw.system.entity.ResourceAndRole;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-11-27
 */
@Service
@Transactional
public class ResourceAndRoleService extends MyService<ResourceAndRoleMapper, ResourceAndRole> {

    /**
     *  角色的资源操作
     * @param roleId
     * @param resourceIds
     * @return
     */
    public Integer operationResourceAndRole(String roleId,String resourceIds) {
        Integer code = 1;
        List<String> resourceIdList = new ArrayList<>();
        if (StringUtils.isNotEmpty(resourceIds)) {
            resourceIdList = Arrays.asList(resourceIds.split(","));
        }
        List<ResourceAndRole> resourceAndRoleList = this.selectList(new ResourceAndRole(roleId));
        if (resourceIdList.size() > 0 && resourceAndRoleList.size() > 0) {
            //获取拥有相同的resourceId
            List<String> commentResourceIdList = new ArrayList<>();
            for (String string: resourceIdList
            ) {
                for (ResourceAndRole resourceAndRole: resourceAndRoleList
                ) {
                    if (resourceAndRole.getMsResourceId().equals(string)) {
                        commentResourceIdList.add(string);
                        break;
                    }
                }
            }
            //获取传入数据与数据库数据相同数据的集合数据总数
            Integer size = commentResourceIdList.size();
            //将新选择的资源加入资源与角色表
a:            for (String string: resourceIdList
            ) {
                for (int i = 0; i < size; i++) {
                    //相同就跳出
                    if (commentResourceIdList.get(i).equals(string)) {
                        break;
                    } else if (i == size - 1) {
                        //最后一个没有找到则添加
                        ResourceAndRole resourceAndRole = new ResourceAndRole();
                        resourceAndRole.setMsRoleId(roleId);
                        resourceAndRole.setMsResourceId(string);
                        code = this.insert(resourceAndRole);
                        if (code < 1) {
                            //跳出命名为a的循环
                            break a;
                        }
                    }
                }
            }
            if (code > 0) {
                //删除没选择的旧数据
b:               for (ResourceAndRole resourceAndRole: resourceAndRoleList
                ) {
                    for (int i = 0; i < size; i++) {
                        //相同就跳出
                        if (commentResourceIdList.get(i).equals(resourceAndRole.getMsResourceId())) {
                            break ;
                        } else if (i == size - 1) {
                            //最后一个没有找到则删除
                            code = this.delete(resourceAndRole);
                            if (code < 1) {
                                //跳出命名为a的循环
                                break b;
                            }
                        }
                    }
                }
            }
        } else if (resourceIdList.size() > 0) {
            //传入的资源编号集合有数据但没查找到该角色数据
            for (String string: resourceIdList
                 ) {
                ResourceAndRole resourceAndRole = new ResourceAndRole(roleId);
                resourceAndRole.setMsResourceId(string);
                code = this.insert(resourceAndRole);
                if (code < 1) {
                    break;
                }
            }
        } else if (resourceAndRoleList.size() > 0) {
            //传入的资源编号集合没有数据但查找到该角色数据
            for (ResourceAndRole resourceAndRole: resourceAndRoleList
            ) {
                code = this.delete(resourceAndRole);
                if (code < 1) {
                    break;
                }
            }
        } else {
            //全没数据
            code = 8;
        }
        if (code < 1) {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

}
