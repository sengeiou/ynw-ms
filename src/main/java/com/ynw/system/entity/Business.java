package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 行业模板
 *  @author ChengZhi
 *  @version 2018-12/7
 */
@Data
@Table(name = "t_bd_business")
@ApiModel(value = "Business", description = "行业模板")
public class Business extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.bdBusinessId=id;
    }

    @Id
    @ApiModelProperty(value = "行业id")
    private String bdBusinessId;//行业id

    @ApiModelProperty(value = "行业名称")
    private String name;//行业名称

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public Business(String bdBusinessId) {
        this.bdBusinessId = bdBusinessId;
    }

    public Business() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }
}
