package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 系统参数配置模板
 *  @author ChengZhi
 *  @version 2018-12/5
 */
@Data
@Table(name = "t_sy_para_conf")
@ApiModel(value = "ParaConf", description = "系统参数配置模板")
public class ParaConf extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.syParaConfId=id;
    }

    @Id
    @ApiModelProperty(value = "系统参数配置ID")
    private String syParaConfId;//系统参数配置ID

    @Column(name = "`describe`")
    @ApiModelProperty(value = "描述")
    private String describe;//描述

    @Column(name = "`key`")
    @ApiModelProperty(value = "键")
    private String key;//键

    @ApiModelProperty(value = "值")
    private String value;//值

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public ParaConf(String syParaConfId) {
        this.syParaConfId = syParaConfId;
    }

    public ParaConf() {
    }
}
