package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 县区模板
 *  @author ChengZhi
 *  @version 2018-12/10
 */
@Data
@Table(name = "t_bd_district")
@ApiModel(value = "District", description = "县区模板")
public class District extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.bdDistrictId=id;
    }

    @Id
    @ApiModelProperty(value = "区县id")
    private String bdDistrictId;//区县id

    @ApiModelProperty(value = "区县名称")
    private String name;//区县名称

    @ApiModelProperty(value = "所属城市id")
    private String bdCityId;//所属城市id

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public District(String bdDistrictId) {
        this.bdDistrictId = bdDistrictId;
    }

    public District() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }

}
