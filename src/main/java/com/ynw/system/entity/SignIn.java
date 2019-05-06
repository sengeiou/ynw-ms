package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * 签到模板
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@Data
@ApiModel(value = "SignIn", description = "签到模板")
public class SignIn extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.tkSigninFlowId=id;
    }

    @Id
    @ApiModelProperty(value = "签到流水id")
    private String tkSigninFlowId;//签到流水id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "签到日")
    private String signinDate;//签到日

    @ApiModelProperty(value = "用户头像")
    private String acUserImg;//用户头像

    @ApiModelProperty(value = "用户昵称")
    private String acUserName;//用户昵称

    @ApiModelProperty(value = "用户电话")
    private String acUserPhone;//用户电话

}
