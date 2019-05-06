package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 活动模板
 * @author ChengZhi
 * @version 2019/1/19
 */
@Data
@Table(name = "t_at_register")
@ApiModel(value = "Register", description = "活动模板")
public class Register extends DateEntity {

    @Override
    public void setId(String id) {
        this.atRegisterId = id;
    }

    @Id
    @ApiModelProperty(value = "活动登记id")
    private String atRegisterId;//活动登记id

    @ApiModelProperty(value = "活动分类id")
    private String atCtgyId;//活动分类id

    @Column(name = "`name`")
    @ApiModelProperty(value = "活动名称")
    private String name;//活动名称

    @ApiModelProperty(value = "活动开始时间")
    private Date beginTime;//活动开始时间

    @ApiModelProperty(value = "活动结束时间")
    private Date endTime;//活动结束时间

    @ApiModelProperty(value = "报名开始时间")
    private Date applyBeginTime;//报名开始时间

    @ApiModelProperty(value = "报名结束时间")
    private Date applyEndTime;//报名结束时间

    @ApiModelProperty(value = "活动海报")
    private String bgImageUrl;//活动海报

    @ApiModelProperty(value = "活动内容描述")
    @Column(name = "`content`")
    private String content;//活动内容描述，根据显示的需要可存富文本源码。

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;//更新时间

    @Transient
    @ApiModelProperty(value = "活动分类名")
    private String classifyName;//活动分类名

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    @Transient
    @ApiModelProperty(value = "报名人数", example = "0")
    private Integer registrationNumber;//报名人数

    public Register(String atRegisterId) {
        this.atRegisterId = atRegisterId;
    }

    public Register() {
    }

    @Override
    public void preInsert() {
        updateTime = new Date();
        super.preInsert();
    }

    @Override
    public void preUpdate() {
        updateTime = new Date();
        super.preUpdate();
    }
}
