package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台角色模板
 *  @author ChengZhi
 *  @version 2018-11-24
 */
@Data
@Table(name = "t_ms_role")
@ApiModel(value = "Role", description = "后台角色模板")
public class Role extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.msRoleId=id;
    }

    @Id
    @ApiModelProperty(value = "角色ID")
    private String msRoleId;//角色ID

    @ApiModelProperty(value = "角色名称")
    private String name;//角色名称

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;//更新时间

    @ApiModelProperty(value = "角色Key")
    private String roleKey;//角色Key

    //关闭mapper数据库映射
    @Transient
    @ApiModelProperty(value = "角色的资源集合")
    private List<Resource> resourceList = new ArrayList<>();//角色的资源集合

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    @Transient
    @ApiModelProperty(value = "角色的账号集合")
    private List<User> userList = new ArrayList<>();//角色的账号集合

    public Role(String msRoleId) {
        this.msRoleId = msRoleId;
    }

    public Role() {
    }

    @Override
    public void preInsert() {
//        Subject subject = SecurityUtils.getSubject();
//        User user = (User) subject.getSession().getAttribute("user");
//        if (null != user){
//            this.roleKey = user.getMsUserId();
//        }
        this.updateTime = new Date();
        super.preInsert();
    }

    @Override
    public void preUpdate() {
        this.updateTime = new Date();
    }
}
