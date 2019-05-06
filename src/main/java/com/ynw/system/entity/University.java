package com.ynw.system.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 高校模板
 *  @author ChengZhi
 *  @version 2018-12/8
 */
@Data
@Table(name = "t_bd_university")
@ApiModel(value = "University", description = "高校模板")
public class University extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.bdUniversityId=id;
    }

    @Id
    @ApiModelProperty(value = "高校id")
    private String bdUniversityId;//高校id

    @ApiModelProperty(value = "高校名称")
    private String name;//高校名称

    @ApiModelProperty(value = "高校所在的城市id")
    private String bdCityId;//高校所在的城市id

    @ApiModelProperty(value = "城市名")
    @Transient
    private String city;//城市名

    @Transient
    @ApiModelProperty(value = "省份")
    private String province;//省份

    @Transient
    @ApiModelProperty(value = "省份编号")
    private String provinceId;//省份编号

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

//    @Transient
//    private List<City> cityList = new ArrayList<>();//省份下的城市集合

    public University(String bdUniversityId) {
        this.bdUniversityId = bdUniversityId;
    }

    public University() {
    }


}
