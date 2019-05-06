package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台管理员模板
 *  @author ChengZhi
 *  @version 2018-11-24
 */
@Data
@Table(name = "t_ms_user")
@ApiModel(value = "User", description = "后台管理员模板")
public class User extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(value = "账户ID")
    private String msUserId;//账户ID

    @ApiModelProperty(value = "账户密码")
    private String password;//账户密码

    @ApiModelProperty(value = "账户名")
    private String name;//账户名

    @ApiModelProperty(value = "真实姓名")
    private String realName;//真实姓名

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;//手机号码

    @ApiModelProperty(value = "注册IP")
    private String registerIp;//注册IP

    @ApiModelProperty(value = "最后登录时间")
    private Date lastLoginTime;//最后登录时间

    @ApiModelProperty(value = "最后登录IP")
    private String lastLoginIp;//最后登录IP

    @ApiModelProperty(value = "登录次数", dataType = "Long", example = "0")
    private Integer loginNum;//登录次数

    @ApiModelProperty(value = "状态", dataType = "Long", example = "1")
    private Integer status;//状态（0：无效   1：有效）

    @ApiModelProperty(value = "邮箱")
    private String email;//邮箱

    @ApiModelProperty(value = "短信验证码")
    private String smsVcode;//短信验证码

    @ApiModelProperty(value = "性别", dataType = "性别", example = "0")
    private Integer sex;//性别（0：男   1：女）

    //关闭mapper数据库映射
    @Transient
    @ApiModelProperty(value = "权限集合")
    private List<Role> roleList = new ArrayList<>();//权限集合

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    @Transient
    @ApiModelProperty(value = "角色编号")
    private String roleId;

    public User() {
    }

    public User(String msUserId) {
        this.msUserId = msUserId;
    }

    @Override
    public void preInsert() {
        if(null == this.status) {
            this.status = 1;
        }
        this.lastLoginTime = new Date();
        this.loginNum = 0;
        super.preInsert();
    }

    @Override
    public void preUpdate() {
        this.lastLoginTime = new Date();
    }
}
