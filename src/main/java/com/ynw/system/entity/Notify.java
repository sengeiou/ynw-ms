package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 推送消息模板
 *  @author ChengZhi
 *  @version 2018-12/13
 */
@Data
@Table(name = "t_ac_s_notify")
@ApiModel(value = "Notify", description = "推送消息模板")
public class Notify extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acSNotifyId=id;
    }

    @Override
    public void preInsert() {
        if (null == this.admireFlag)
            this.admireFlag = 1;
        if (null == this.attentionFlag)
            this.attentionFlag = 0;
        if (null == this.commentFlag)
            this.commentFlag = 1;
        if (null == this.dStateFlag)
            this.dStateFlag = 0;
        if (null == this.vUpdateFlag)
            this.vUpdateFlag = 0;
        if (null == this.addFriendFlag)
            this.addFriendFlag = 1;
        super.preInsert();
    }

    @Id
    @ApiModelProperty(value = "消息通知设置id")
    private String acSNotifyId;//消息通知设置id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "点赞通知标识", dataType = "Long", example = "0")
    private Integer admireFlag;//点赞通知标识（1：通知   0：不通知）

    @ApiModelProperty(value = "关注通知标识", dataType = "Long", example = "0")
    private Integer attentionFlag;//关注通知标识（1：通知   0：不通知）

    @ApiModelProperty(value = "评论通知标识", dataType = "Long", example = "0")
    private Integer commentFlag;//评论通知标识（1：通知   0：不通知）

    @ApiModelProperty(value = "动态通知标识", dataType = "Long", example = "0")
    private Integer dStateFlag;//动态通知标识（1：通知   0：不通知）

    @ApiModelProperty(value = "版本更新通知标识", dataType = "Long", example = "0")
    private Integer vUpdateFlag;//版本更新通知标识（1：通知   0：不通知）

    @ApiModelProperty(value = "好友请求通知标识", dataType = "Long", example = "0")
    private Integer addFriendFlag;//好友请求通知标识（1：通知   0：不通知）

    public Notify(String acUserId) {
        this.acUserId = acUserId;
    }

    public Notify() {
    }
}
