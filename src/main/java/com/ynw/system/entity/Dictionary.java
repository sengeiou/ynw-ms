package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 字典模板
 *  @author ChengZhi
 *  @version 2018/12/6
 */
@Data
@Table(name = "t_sy_dictionary")
@ApiModel(value = "Dictionary", description = "字典模板")
public class Dictionary implements Entity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.syDictionaryId=id;
    }

    @Id
    @ApiModelProperty(value = "字典ID")
    private String syDictionaryId;//字典ID

    @ApiModelProperty(value = "组键")
    private String groupKey;//组键

    @ApiModelProperty(value = "组描述")
    private String groupDesc;//组描述

    @ApiModelProperty(value = "键")
    private String itemKey;//键

    @ApiModelProperty(value = "值")
    private String itemValue; //值

    @ApiModelProperty(value = "描述")
    @Column(name = "`describe`")
    private String describe;//描述

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    public Dictionary(String syDictionaryId) {
        this.syDictionaryId = syDictionaryId;
    }

    public Dictionary() {
    }

    @Transient
    private String LogContent;//日志主体

    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
    }

}
