package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 城市模板
 *  @author ChengZhi
 *  @version 2018-12/8
 */
@Data
@Table(name = "t_bd_city")
@ApiModel(value = "City", description = "城市模板")
public class City extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.bdCityId=id;
    }

    @Id
    @ApiModelProperty(value = "城市ID")
    private String bdCityId;//城市ID

    @ApiModelProperty(value = "城市名称")
    private String name;//城市名称

    @ApiModelProperty(value = "所属省份ID")
    private String bdProvinceId;//所属省份ID

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public City(String bdCityId) {
        this.bdCityId = bdCityId;
    }

    public City() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }
}
