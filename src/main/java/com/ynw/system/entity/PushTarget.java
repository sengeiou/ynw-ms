package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 推送消息接受状态模板
 *  @author ChengZhi
 *  @version 2018/3/5
 */
@Data
@Table(name = "t_mg_target")
@ApiModel(value = "PushTarget", description = "推送消息接受状态模板")
public class PushTarget implements Entity {

    @Id
    @ApiModelProperty(value = "消息目标id")
    private String mgTargetId;//消息目标id

    @Override
    public void setId(String id) {
        this.mgTargetId = id;
    }

    @ApiModelProperty(value = "消息体id")
    private String mgBodyId;//消息体id

    @ApiModelProperty(value = "消息目标")
    private String target;//消息目标，推送消息所发送的目标标识，可用用户id标识。

    @ApiModelProperty(value = "是否已读", dataType = "Long", example = "0")
    private Integer isRead;//是否已读（0：表示未读    1：表示已读）

    @ApiModelProperty(value = "消息已读时间")
    private Date readTime;//消息已读时间

    public PushTarget(String mgBodyId, String target, Integer isRead) {
        this.mgBodyId = mgBodyId;
        this.target = target;
        this.isRead = isRead;
    }

    public PushTarget() {
    }
}
