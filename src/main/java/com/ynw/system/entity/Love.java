package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * 情豆模板
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@Data
@ApiModel(value = "Love", description = "情豆模板")
public class Love extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acBeanFlowId=id;
    }

    @Id
    @ApiModelProperty(value = "情豆流水id")
    private String acBeanFlowId;//情豆流水id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "情豆出入账数量")
    private String quantity;//情豆出入账数量

    @ApiModelProperty(value = "情豆出入账类型")
    private String type;//情豆出入账类型（0：情豆支出    1：情豆收入）

    @ApiModelProperty(value = "关联业务key")
    private String assoBusinessKey;//关联业务key，在字典表中取值，表示情豆获取或支出的渠道和方式，比如签到、发表话题、打赏等等。

    @ApiModelProperty(value = "关联业务id")
    private String assoBusinessId;//关联业务id，即在哪获取的、在哪消耗的相关业务记录id，比如签到获取的情豆这里则存储签到流水表中相关记录的id。

    @ApiModelProperty(value = "字典key对应的值")
    private String assoBusinessName;//字典key对应的值

    @ApiModelProperty(value = "用户昵称")
    private String acUserName;//用户昵称

    @ApiModelProperty(value = "手机号码")
    private String acUserPhone;//手机号码

    @ApiModelProperty(value = "用户头像")
    private String acUserImg;//用户头像

}
