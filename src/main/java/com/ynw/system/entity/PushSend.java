package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 推送消息发送状态模板
 *  @author ChengZhi
 *  @version 2018/3/5
 */
@Data
@Table(name = "t_mg_send")
@ApiModel(value = "PushSend", description = "推送消息发送状态模板")
public class PushSend implements Entity {

    @Id
    @ApiModelProperty(value = "消息发送id")
    private String mgSendId;//消息发送id

    @Override
    public void setId(String id) {
        this.mgSendId = id;
    }

    @ApiModelProperty(value = "消息体id")
    private String mgBodyId;//消息体id

    @ApiModelProperty(value = "消息发送状态", dataType = "Long", example = "1")
    private Integer status;//消息发送状态（0：未发送    1：已发送）

    @ApiModelProperty(value = "消息发送定时器")
    private Date timer;//消息发送定时器

    @ApiModelProperty(value = "消息发送时间")
    private Date sendTime;//消息发送时间

    public PushSend(String mgBodyId) {
        this.mgBodyId = mgBodyId;
    }

    public PushSend() {
    }
}
