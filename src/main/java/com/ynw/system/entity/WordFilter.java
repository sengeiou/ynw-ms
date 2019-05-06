package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 敏感词模板
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@Data
@Table(name = "t_sy_wordfilter")
@ApiModel(value = "WordFilter", description = "敏感词模板")
public class WordFilter extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.syWordfilterId=id;
    }

    @Id
    @ApiModelProperty(value = "敏感词过滤id")
    private String syWordfilterId;//敏感词过滤id

    @ApiModelProperty(value = "类别")
    private String category;//类别，取字典表中定义的key。

    @ApiModelProperty(value = "敏感词")
    private String word;//敏感词

    @ApiModelProperty(value = "状态", dataType = "Long", example = "1")
    private Integer status;//状态（1：有效   0：无效）

    @Transient
    @ApiModelProperty(value = "字典表value")
    private String categoryName;//字典表value

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public WordFilter(String syWordfilterId) {
        this.syWordfilterId = syWordfilterId;
    }

    public WordFilter() {
    }

    @Override
    public void preInsert() {
        if (null == status) {
            status = 1;
        }
        super.preInsert();
    }
}
