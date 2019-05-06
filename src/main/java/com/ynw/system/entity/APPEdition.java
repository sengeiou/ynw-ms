package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * APP版本模板
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@Data
@Table(name = "t_ms_app_v_update")
@ApiModel(value = "APPEdition", description = "APP版本模板")
public class APPEdition extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.msAppVUpdateId=id;
    }

    @Id
    @ApiModelProperty(value = "版本的id")
    private String msAppVUpdateId;//版本的id

    @ApiModelProperty(value = "版本号")
    private String number;//版本号

    @ApiModelProperty(value = "版本内容")
    private String content;//版本内容

    @ApiModelProperty(value = "安装包url")
    private String packageUrl;//安装包url

    @ApiModelProperty(value = "是否强制更新", example = "0")
    private Integer isForceUpdate;//是否强制更新（0:不强制  1:强制）

    @ApiModelProperty(value = "版本更新描述")
    @Column(name = "`describe`")
    private String describe;//版本更新描述

    @ApiModelProperty(value = "系统类型")
    private String visitorOsType;//系统类型，ios还是android，取字典表key标识

    @ApiModelProperty(value = "版本大小")
    private String size;//版本大小

    @Transient
    @ApiModelProperty(value = "系统类型名字")
    private String visitorOsTypeName;

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public APPEdition(String msAppVUpdateId) {
        this.msAppVUpdateId = msAppVUpdateId;
    }

    public APPEdition() {
    }

    @Override
    public void preInsert() {
        if (null == isForceUpdate) {
            isForceUpdate = 0;
        }
        super.preInsert();
    }
}
