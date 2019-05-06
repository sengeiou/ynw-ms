package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Statistics", description = "统计模板")
public class Statistics {

    @ApiModelProperty(value = "用户总数", dataType = "Long", example = "0")
    private Integer acUserCount = 0; //用户总数

    @ApiModelProperty(value = "新增用户数", dataType = "Long", example = "0")
    private Integer nowAcUserCount = 0;//新增用户数

    @ApiModelProperty(value = "新增关注关系", dataType = "Long", example = "0")
    private Integer nowAttention = 0;//新增关注关系

    @ApiModelProperty(value = "新增好友关系", dataType = "Long", example = "0")
    private Integer nowFriend = 0;//新增好友关系

    @ApiModelProperty(value = "新增签到", dataType = "Long", example = "0")
    private Integer nowSignIn = 0;//新增签到

    @ApiModelProperty(value = "新增动态", dataType = "Long", example = "0")
    private Integer nowMood = 0;//新增动态

    @ApiModelProperty(value = "情豆发放", dataType = "Long", example = "0")
    private Integer nowLoveGrant = 0;//情豆发放

    @ApiModelProperty(value = "情豆回收", dataType = "Long", example = "0")
    private Integer nowLoveRecycle = 0;//情豆回收

    @ApiModelProperty(value = "情豆转移", dataType = "Long", example = "0")
    private Integer nowLoveTransfer = 0;//情豆转移

    @ApiModelProperty(value = "问卷测试数", dataType = "Long", example = "0")
    private Integer nowExamText = 0;//问卷测试数

    @ApiModelProperty(value = "测试人数", dataType = "Long", example = "0")
    private Integer nowExamUserNum = 0;//测试人数

}
