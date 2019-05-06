package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * 好友关系模板
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@Data
@ApiModel(value = "Friend", description = "好友关系模板")
public class Friend extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acFriendRelId=id;
    }

    @Id
    @ApiModelProperty(value = "用户好友关系id")
    private String acFriendRelId;//用户好友关系id

    @ApiModelProperty(value = "好友关系请求方用户id")
    private String reqUserId;//好友关系请求方用户id

    @ApiModelProperty(value = "好友关系应答方用户id")
    private String resUserId;//好友关系应答方用户id

    @ApiModelProperty(value = "请求方")
    private AcUser reqUser = new AcUser();//请求方

    @ApiModelProperty(value = "应答方")
    private AcUser resUser = new AcUser();//应答方

}
