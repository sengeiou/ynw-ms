package com.ynw.system.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 学历模板
 *  @author ChengZhi
 *  @version 2018-12/8
 */
@Data
@Table(name = "t_bd_degrees")
@ApiModel(value = "Degrees", description = "学历模板")
public class Degrees extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.bdDegreesId=id;
    }

    @Id
    @ApiModelProperty(value = "学历id")
    private String bdDegreesId;//学历id

    @ApiModelProperty(value = "学历名称")
    private String name;//学历名称

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public Degrees(String bdDegreesId) {
        this.bdDegreesId = bdDegreesId;
    }

    public Degrees() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }

}
