package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 约活动主题模板
 * @author ChengZhi
 * @version 2019/4/26
 */
@Data
@Table(name = "t_at_invite_theme")
@ApiModel(value = "InviteTheme", description = "约活动主题模板")
public class InviteTheme extends DateEntity {

    @Override
    public void setId(String id) {
        this.atInviteThemeId = id;
    }

    @Id
    @ApiModelProperty(value = "约活动主题id")
    private String atInviteThemeId;//约活动主题id

    @Column(name = "`name`")
    @ApiModelProperty(value = "主题名称")
    private String name;//主题名称

    @Column(name = "`icon`")
    @ApiModelProperty(value = "icon图标")
    private String icon;//icon图标

    @ApiModelProperty(value = "每个类别有不同的URL")
    private String bgImageUrl;//每个类别有不同的URL

    @Column(name = "`sort`")
    @ApiModelProperty(value = "排序", example = "1", dataType = "Long")
    private Integer sort;//排序

    @Column(name = "`status`")
    @ApiModelProperty(value = "约活动主题状态", example = "1", dataType = "Long")
    private Integer status;//约活动主题状态 0: 删除 1:可用

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    @Override
    public void preInsert() {
        if (null == this.status)
            this.status = 1;
        super.preInsert();
    }

    public InviteTheme(String atInviteThemeId) {
        this.atInviteThemeId = atInviteThemeId;
    }

    public InviteTheme() {
    }
}
