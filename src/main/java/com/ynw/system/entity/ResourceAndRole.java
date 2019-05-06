package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 管理员与角色表模板
 *  @author ChengZhi
 *  @version 2018-11-27
 */
@Data
@Table(name = "t_ms_role_resource")
@ApiModel(value = "ResourceAndRole", description = "管理员与角色表模板")
public class ResourceAndRole extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.msRoleResourceId=id;
    }

    @Id
    @ApiModelProperty(value = "账户角色关联ID")
    private String msRoleResourceId;//账户角色关联ID

    @ApiModelProperty(value = "资源ID")
    private String msResourceId;//资源ID

    @ApiModelProperty(value = "角色ID")
    private String msRoleId;//角色ID

    public ResourceAndRole(String msRoleId) {
        this.msRoleId = msRoleId;
    }

    public ResourceAndRole() {
    }
}
