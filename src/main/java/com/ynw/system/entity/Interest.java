package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 兴趣标签模板
 *  @author ChengZhi
 *  @version 2018-12/10
 */
@Data
@Table(name = "t_bd_interest")
@ApiModel(value = "Interest", description = "兴趣标签模板")
public class Interest extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.bdInterestId=id;
    }

    @Id
    @ApiModelProperty(value = "兴趣标签id")
    private String bdInterestId;//兴趣标签id

    @ApiModelProperty(value = "兴趣标签名称")
    private String name;//兴趣标签名称

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public Interest(String bdInterestId) {
        this.bdInterestId = bdInterestId;
    }

    public Interest() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }

}
