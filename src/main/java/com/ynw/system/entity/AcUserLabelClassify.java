package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "t_ac_label_ctgy")
@ApiModel(value = "AcUserLabelClassify", description = "用户标签分类")
public class AcUserLabelClassify extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acLabelCtgyId = id;
    }

    @Id
    @ApiModelProperty(value = "用户标签id")
    private String acLabelCtgyId;//用户标签id

    @Column(name = "`key`")
    @ApiModelProperty(value = "标签分类自定义key")
    private String key;//标签分类自定义key

    @Column(name = "`name`")
    @ApiModelProperty(value = "标签内容")
    private String name;//标签内容

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public AcUserLabelClassify(String acLabelCtgyId) {
        this.acLabelCtgyId = acLabelCtgyId;
    }

    public AcUserLabelClassify() {
    }
}
