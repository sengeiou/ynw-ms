package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 系统日志模板
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@Data
@Table(name = "t_ms_opt_log")
@ApiModel(value = "OptLog", description = "系统日志模板")
public class OptLog extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.msOptLogId=id;
    }

    @Id
    @ApiModelProperty(value = "操作日志id")
    private String msOptLogId;//操作日志id

    @ApiModelProperty(value = "操作员id")
    private String msUserId;//操作员id

    @ApiModelProperty(value = "登录ip")
    private String loginIp;//登录ip

    @ApiModelProperty(value = "操作记录")
    private String content;//操作记录

    @Transient
    @ApiModelProperty(value = "操作员名字")
    private String msUserName;//操作员名字

}
