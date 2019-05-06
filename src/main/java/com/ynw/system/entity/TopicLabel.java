package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 话题标签模板
 *  @author ChengZhi
 *  @version 2019-2-15
 */
@Data
@Table(name = "t_ds_label")
@ApiModel(value = "TopicLabel", description = "话题标签模板")
public class TopicLabel implements Entity {
    @Override
    public void setId(String id) {
        this.dsLabelId = id;
    }

    @Id
    @ApiModelProperty(value = "话题标签id")
    private String dsLabelId;//动态标签id

    @ApiModelProperty(value = "标签名称")
    private String name;//标签名称

    @ApiModelProperty(value = "是否被强调", dataType = "Long", example = "0")
    private Integer isEmphasized;//是否被强调（1：是    0：否）

    @ApiModelProperty(value = "是否被隐藏", dataType = "Long", example = "0")
    private Integer isHidden;//是否被隐藏（0：不隐藏   1：隐藏）

    @ApiModelProperty(value = "排序", dataType = "Long", example = "1")
    private Integer sort;//排序

    @ApiModelProperty(value = "添加系统日志的主体")
    @Transient
    private String LogContent;//添加系统日志的主体

    public TopicLabel(String dsLabelId) {
        this.dsLabelId = dsLabelId;
    }

    public TopicLabel() {
    }

    public void preInsert() {
        if (null == this.sort) {
            this.sort = 1;
        }
        if (null == this.isEmphasized) {
            this.isEmphasized = 0;
        }
        if (null == this.isHidden) {
            this.isHidden = 0;
        }
    }

}
