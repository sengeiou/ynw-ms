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
 * 推送消息主体模板
 *  @author ChengZhi
 *  @version 2018/3/5
 */
@Data
@Table(name = "t_mg_body")
@ApiModel(value = "PushBody", description = "推送消息主体模板")
public class PushBody extends DateEntity {

    @Override
    public void setId(String id) {
        this.mgBodyId = id;
    }

    @Id
    @ApiModelProperty(value = "消息体id")
    private String mgBodyId;//消息体id

    @Column(name = "`type`")
    @ApiModelProperty(value = "消息类型")
    private String type;//消息类型，取字典表中定义的key

    @Column(name = "`group`")
    @ApiModelProperty(value = "消息分组")
    private String group;//消息分组，取字典表中定义的key

    @ApiModelProperty(value = "消息目标范围")
    private String sendScope;//消息目标范围，取字典表中定义的key

    @ApiModelProperty(value = "消息标题")
    @Column(name = "`title`")
    private String title;//消息标题

    @Column(name = "`content`")
    @ApiModelProperty(value = "消息内容")
    private String content;//消息内容

    @ApiModelProperty(value = "图片文件路径")
    private String imageUrl;//图片文件路径

    @ApiModelProperty(value = "消息网页地址")
    private String webUrl;//消息网页地址

    @ApiModelProperty(value = "目标功能模块名")
    private String targetModuleName;//目标功能模块名，取字典表中定义的key。

    @ApiModelProperty(value = "目标功能模块参数")
    private String targetModulePara;//目标功能模块参数

    @ApiModelProperty(value = "消息开始时间")
    private Date beginTime;//消息开始时间

    @ApiModelProperty(value = "消息结束时间")
    private Date endTime;//消息结束时间

    public PushBody() {
    }

    public PushBody(String type, String group, String sendScope, String title, String content) {
        this.type = type;
        this.group = group;
        this.sendScope = sendScope;
        this.title = title;
        this.content = content;
    }

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序", dataType = "Long", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "日志主体")
    private String LogContent;//日志主体

    @Transient
    @ApiModelProperty(value = "消息类型名")
    private String typeName;//消息类型名

    @Transient
    @ApiModelProperty(value = "消息分组名")
    private String groupName;//消息分组名

    @Transient
    @ApiModelProperty(value = "消息目标范围名")
    private String sendScopeName;//消息目标范围名

    @Transient
    @ApiModelProperty(value = "目标功能模块")
    private String targetModule;//目标功能模块

    @Transient
    @ApiModelProperty(value = "消息业务类型")
    private String businessType;//消息业务类型

    @Transient
    @ApiModelProperty(value = "消息是否已经发送", dataType = "Long", example = "0")
    private Integer send;//消息是否已经发送

    @Override
    public void preInsert() {
        if (null == this.sort)
            this.sort = 1;
        super.preInsert();
    }
}
