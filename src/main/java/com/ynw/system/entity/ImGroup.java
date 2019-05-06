package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 环信群组模板
 *  @author ChengZhi
 *  @version 2019/3/11
 */
@Data
@Table(name = "t_im_group")
@ApiModel(value = "ImGroup", description = "环信群组模板")
public class ImGroup extends DateEntity {

    @Override
    public void preInsert() {
        reuse();
        super.preInsert();
    }

    /**
     *  添加和更新复用
     */
    private void reuse() {
        if(null == this.isPublic)
            this.isPublic = 1;
        if(null == this.isJoincheck)
            this.isJoincheck = "0";
        if(null == this.isAllowinvite)
            this.isAllowinvite = "0";
        if(null == this.inviteNeedConfirm)
            this.inviteNeedConfirm = 1;
        if(null == this.maxusers)
            this.maxusers = 201;
        if(null == this.status)
            this.status = 1;
    }

    @Override
    public void preUpdate() {
        reuse();
        super.preUpdate();
    }

    @Id
    @ApiModelProperty(value = "聊天群组id")
    private String imGroupId;//聊天群组id

    @ApiModelProperty(value = "对应的环信群组id")
    private String hxGroupId;//对应的环信群组id

    @Column(name = "`name`")
    @ApiModelProperty(value = "群组名称")
    private String name;//群组名称

    @Column(name = "`des`")
    @ApiModelProperty(value = "群组描述")
    private String des;//群组描述

    @ApiModelProperty(value = "是否是公开群", example = "1")
    private Integer isPublic;//是否是公开群（0：否，表示私有群    1：是，表示公开群）

    @ApiModelProperty(value = "入群是否需要群主或管理员审核")
    private String isJoincheck;//入群是否需要群主或管理员审核（0：不需要   1：需要）

    @ApiModelProperty(value = "是否允许群成员邀请别人加入此群")
    private String isAllowinvite;//是否允许群成员邀请别人加入此群（0：不允许   1：允许）

    @ApiModelProperty(value = "邀请加群", example = "0")
    private Integer inviteNeedConfirm;//邀请加群，被邀请人是否需要确认（0：不需要确认   1：需要确认）

    @Column(name = "`maxusers`")
    @ApiModelProperty(value = "群成员上限", example = "2000")
    private Integer maxusers;//群成员上限

    @Column(name = "`owner`")
    @ApiModelProperty(value = "群主的用户id")
    private String owner;//群主的用户id

    @ApiModelProperty(value = "业务类型")
    private String businessType;//业务类型，取字典表中定义的key。

    @ApiModelProperty(value = "业务id")
    private String businessId;//业务id

    @Column(name = "`status`")
    @ApiModelProperty(value = "状态",example = "1")
    private Integer status;//状态（1：正常   -1：已解散）

    @ApiModelProperty(value = "一周情侣的活动匹配编号")
    private String matchNo;//一周情侣的活动匹配编号

    @ApiModelProperty(value = "群的用途类型", example = "0")
    private Integer useType;//群的用途类型（0：大群   1：CP房间 2:活动辅群）

    public ImGroup() {
    }

    public ImGroup(String imGroupId, String hxGroupId, String name,
                   String des, Integer isPublic, String isJoincheck,String isAllowinvite,
                   Integer maxusers, String owner) {
        this.imGroupId = imGroupId;
        this.hxGroupId = hxGroupId;
        this.name = name;
        this.des = des;
        this.isPublic = isPublic;//是否公开
        this.isJoincheck = isJoincheck;//加入群是否需要群主肯定
        this.isAllowinvite =isAllowinvite ;//是否允许邀请
        this.inviteNeedConfirm = 0;//是否邀请人需要确定,服务端创建默认不需要
        this.maxusers = maxusers;
        this.owner = owner;
        preInsert();
    }

    public ImGroup(Integer useType, String businessType, String businessId, Integer status) {
        this.useType = useType;
        this.businessType = businessType;
        this.businessId = businessId;
        this.status = status;
    }

    public ImGroup(String name, String businessType, String businessId, Integer status, Integer useType) {
        this.name = name;
        this.businessType = businessType;
        this.businessId = businessId;
        this.status = status;
        this.useType = useType;
    }
    public ImGroup(String businessType, String businessId, Integer status, String matchNo) {
        this.matchNo = matchNo;
        this.businessType = businessType;
        this.businessId = businessId;
        this.status = status;
    }

    public ImGroup(String businessType, String businessId, Integer status) {
        this.businessType = businessType;
        this.businessId = businessId;
        this.status = status;
    }
}
