package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 约活动报名用户模板
 * @author ChengZhi
 * @version 2019/4/26
 */
@Data
@Table(name = "t_at_invite_user")
@ApiModel(value = "Invite", description = "约活动报名用户模板")
public class Invite extends DateEntity {

    @Id
    @ApiModelProperty(value = "约活动用户报名id")
    private String atInviteUserId;//约活动用户报名id

    @ApiModelProperty(value = "活动登记id")
    private String atRegisterId;//活动登记id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "联系电话")
    private String phoneNumber;//联系电话

    @ApiModelProperty(value = "是否是发起人", example = "0")
    private Integer userType;//是否是发起人 0: 不是 1:是

    @ApiModelProperty(value = "活动报名者的真实姓名")
    private String realName;//活动报名者的真实姓名

    public Invite(String atRegisterId, Integer userType) {
        this.atRegisterId = atRegisterId;
        this.userType = userType;
    }

    public Invite() {
    }
}
