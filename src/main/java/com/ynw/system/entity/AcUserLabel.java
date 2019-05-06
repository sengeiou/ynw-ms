package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Table(name = "t_ac_label")
@ApiModel(value = "AcUserLabel", description = "用户标签")
public class AcUserLabel extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acLabelId = id;
    }

    @Id
    @ApiModelProperty(value = "用户标签id")
    private String acLabelId;//用户标签id

    @ApiModelProperty(value = "用户标签分类id")
    private String acLabelCtgyId;//用户标签分类id

    @Column(name = "`name`")
    @ApiModelProperty(value = "标签内容")
    private String name;//标签内容

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "用户标签分类名")
    private String acLabelClassifyName;//用户标签分类名

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public AcUserLabel(String acLabelId) {
        this.acLabelId = acLabelId;
    }

    public AcUserLabel() {
    }
}
