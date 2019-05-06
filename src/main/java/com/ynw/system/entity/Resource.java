package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 后台资源模板
 *  @author ChengZhi
 *  @version 2018-11-24
 */
@Data
@Table(name = "t_ms_resource")
@ApiModel(value = "Resource", description = "后台资源模板")
public class Resource extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.msResourceId=id;
    }

    @Id
    @ApiModelProperty(value = "资源ID")
    private String msResourceId;//资源ID

    @Column(name = "`describe`")
    @ApiModelProperty(value = "描述")
    private String describe;//描述

    @ApiModelProperty(value = "是否隐藏", dataType = "Long", example = "0")
    private Integer isHide;//是否隐藏（1：是   0：否）

    @ApiModelProperty(value = "层级", dataType = "Long", example = "1")
    private Integer level;//层级

    @ApiModelProperty(value = "资源名")
    private String name;//资源名

    @ApiModelProperty(value = "排序", dataType = "Long", example = "1")
    private Integer sort;//排序

    @ApiModelProperty(value = "来源Key")
    private String sourceKey;//来源Key

    @ApiModelProperty(value = "来源URL")
    private String sourceUrl;//来源URL

    @ApiModelProperty(value = "资源类型", dataType = "Long", example = "0")
    private Integer type;//资源类型（0：目录  1：菜单  2：按钮）

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;//更新时间

    @ApiModelProperty(value = "父资源ID")
    private String parentId;//父资源ID

    @ApiModelProperty(value = "父资源名字")
    @Transient
    private String parentName;//父资源名字

    @Transient
    @ApiModelProperty(value = "子资源集合")
    private List<Resource> resourceList = new ArrayList<>();//子资源集合

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public Resource(String msResourceId) {
        this.msResourceId = msResourceId;
    }

    public Resource() {
    }

    @Override
    public void preInsert() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getSession().getAttribute("user");
        if (null != user){
            if (null == this.sourceKey) {
                this.sourceKey = user.getMsUserId();
            }
        }
        if (null == this.isHide) {
            this.isHide = 0;
        }
        if (null == this.level) {
            this.level = 1;
        }
        if (null == this.sort) {
            this.sort = 1;
        }
        if (null == this.type) {
            this.type = 0;
        }
        this.updateTime = new Date();
        super.preInsert();
    }

    @Override
    public void preUpdate() {
        this.updateTime = new Date();
    }
}
