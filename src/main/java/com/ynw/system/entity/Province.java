package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 省份模板
 *  @author ChengZhi
 *  @version 2018-12/7
 */
@Data
@Table(name = "t_bd_province")
@ApiModel(value = "Province", description = "省份模板")
public class Province extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.bdProvinceId=id;
    }

    @Id
    @ApiModelProperty(value = "省份ID")
    private String bdProvinceId;//省份ID

    @ApiModelProperty(value = "省份名称")
    private String provinceName;//省份名称

    @ApiModelProperty(value = "排序", dataType = "Long", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public Province(String bdProvinceId) {
        this.bdProvinceId = bdProvinceId;
    }

    public Province() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }
}
