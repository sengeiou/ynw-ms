package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 环信群组成员模板
 *  @author ChengZhi
 *  @version 2019/3/11
 */
@Data
@Table(name = "t_im_group_member")
@ApiModel(value = "ImGroupMember", description = "环信群组成员模板")
public class ImGroupMember extends DateEntity {
    @Override
    public void setId(String id) {
        this.imGroupMemberId = id;
    }

    @Override
    public void preInsert() {
        reuse();
        super.preInsert();
    }

    /**
     *  添加和更新复用
     */
    private void reuse() {
        if(null == this.role)
            this.role = 0;
        if(null == this.inBlist)
            this.inBlist = 0;
        if(null == this.isNospeak)
            this.isNospeak = 0;
    }

    @Override
    public void preUpdate() {
        reuse();
        super.preUpdate();
    }

    @Id
    @ApiModelProperty(value = "聊天群组成员id")
    private String imGroupMemberId;//聊天群组成员id

    @ApiModelProperty(value = "聊天群组id")
    private String imGroupId;//聊天群组id

    @ApiModelProperty(value = "群成员用户id")
    private String memberId;//群成员用户id

    @Column(name = "`role`")
    @ApiModelProperty(value = "角色", example = "0")
    private Integer role;//角色（0：普通群员   1：群管理员   2：群主）

    @ApiModelProperty(value = "是否在群黑名单中", example = "0")
    private Integer inBlist;//是否在群黑名单中（0：否   1：是）

    @ApiModelProperty(value = "是否被禁言", example = "0")
    private Integer isNospeak;//是否被禁言（0：否   1：是）

    public ImGroupMember() {
    }

    public ImGroupMember(String imGroupId, String memberId, Integer role){
        this.imGroupId = imGroupId;
        this.memberId = memberId;
        this.role = role;
        preInsert();
    }
}
