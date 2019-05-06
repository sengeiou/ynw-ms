package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 自我标签模板
 *  @author ChengZhi
 *  @version 2018-12/10
 */
@Data
@Table(name = "t_bd_selflabel")
@ApiModel(value = "SelfLabel", description = "自我标签模板")
public class SelfLabel extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.bdSelflabelId=id;
    }

    @Id
    @ApiModelProperty(value = "自我标签id")
    private String bdSelflabelId;//自我标签id

    @ApiModelProperty(value = "自我标签名称")
    private String name;//自我标签名称

    @ApiModelProperty(value = "排序", dataType = "Long", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public SelfLabel(String bdSelflabelId) {
        this.bdSelflabelId = bdSelflabelId;
    }

    public SelfLabel() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }

}
