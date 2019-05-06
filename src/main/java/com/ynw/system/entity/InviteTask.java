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
@Table(name = "t_ac_invite_task")
@ApiModel(value = "InviteTask", description = "邀请任务")
public class InviteTask extends DateEntity {

    @Id
    @ApiModelProperty(value = "城市ID")
    private String acTaskId;//id

    @ApiModelProperty(value = "任务人id")
    private String acUserId;//任务人id

    @ApiModelProperty(value = "任务名称")
    private String taskName;//任务名称，取字典表中的KEY

    @Column(name = "`status`")
    @ApiModelProperty(value = "完成状态", dataType = "Long", example = "0")
    private Integer status;//完成状态 0:未完成 1:已完成

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;//更新时间

    @ApiModelProperty(value = "任务人名字")
    @Transient
    private String userName;//任务人名字

    @Transient
    @ApiModelProperty(value = "任务人电话")
    private String userPhone;//任务人电话

    @Transient
    @ApiModelProperty(value = "邀请人名字")
    private String inviteName;//邀请人名字

    @Transient
    @ApiModelProperty(value = "邀请人电话")
    private String invitePhone;//邀请人电话

    @Transient
    @ApiModelProperty(value = "字典表任务key的值")
    private String taskKeyValue;//字典表任务key的值

    /**
     *  插入之前调用
     */
    public void preInserts(String acUserId, String taskName){
        this.acUserId = acUserId;
        this.taskName = taskName;
        preInsert();
    }

    @Override
    public void preInsert() {
        super.preInsert();
    }
}
