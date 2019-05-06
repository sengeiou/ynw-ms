package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 活动分类模板
 * @author ChengZhi
 * @version 2019/1/19
 */
@Data
@Table(name = "t_at_ctgy")
@ApiModel(value = "RegisterClassify", description = "活动分类模板")
public class RegisterClassify extends DateEntity {

    @Override
    public void setId(String id) {
        this.atCtgyId = id;
    }

    @Id
    @ApiModelProperty(value = "活动分类id")
    private String atCtgyId;//活动分类id

    @ApiModelProperty(value = "活动分类名称")
    @Column(name = "`name`")
    private String name;//活动分类名称

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public RegisterClassify(String atCtgyId) {
        this.atCtgyId = atCtgyId;
    }

    public RegisterClassify() {
    }
}
