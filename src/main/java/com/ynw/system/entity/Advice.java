package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户反馈模板
 *  @author ChengZhi
 *  @version 2018/3/5
 */
@Data
@Table(name = "t_sy_advice")
@ApiModel(value = "Advice", description = "用户反馈模板")
public class Advice extends DateEntity {

    @Id
    @ApiModelProperty(value = "意见反馈id")
    private String syAdviceId;//意见反馈id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "反馈内容类型", example = "0")
    private Integer type;//反馈内容类型（0：文本   1：图片）

    @ApiModelProperty(value = "反馈内容")
    private String content;//反馈内容，根据type字段表示的类型不同分别存储文本内容或图片url。

    @Transient
    @ApiModelProperty(value = "用户姓名")
    private String userName;//用户姓名

    @Transient
    @ApiModelProperty(value = "如果回复则返回回复编号")
    private String businessId;//如果回复则返回回复编号

}
