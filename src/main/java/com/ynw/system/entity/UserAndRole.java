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
@Table(name = "t_ms_user_role")
@ApiModel(value = "UserAndRole", description = "管理员与角色表模板")
public class UserAndRole extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.msUserRoleId=id;
    }

    @Id
    @ApiModelProperty(value = "账户角色关联ID")
    private String msUserRoleId;//账户角色关联ID

    @ApiModelProperty(value = "账户ID")
    private String msUserId;//账户ID

    @ApiModelProperty(value = "角色ID")
    private String msRoleId;//角色ID

    public UserAndRole(String msUserId) {
        this.msUserId = msUserId;
    }

    public UserAndRole() {
    }
}
