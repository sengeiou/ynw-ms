package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 问卷测试模板
 *  @author ChengZhi
 *  @version 2018-12/14
 */
@Data
@ApiModel(value = "ExamPaperTest", description = "问卷测试模板")
public class ExamPaperTest extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.kbExampaperTestId=id;
    }

    @Id
    @ApiModelProperty(value = "问卷测试id")
    private String kbExampaperTestId;//问卷测试id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "问卷id")
    private String kbExampaperId;//问卷id

    @ApiModelProperty(value = "问卷类型")
    private String kbExampaperAnswerId;//问卷类型，指定了该问卷计算答案的方式，取字典表中定义的key。

    @ApiModelProperty(value = "测试的结果分值", example = "0")
    private Integer sumScore;//测试的结果分值，当问卷是按分值计算答案时该字段有值。

//    @Transient
@ApiModelProperty(value = "用户头像")
    private String userImage;//用户头像

//    @Transient
@ApiModelProperty(value = "用户昵称")
    private String userName;//用户昵称

//    @Transient
@ApiModelProperty(value = "用户手机")
    private String userPhone;//用户手机

//    @Transient
@ApiModelProperty(value = "问卷标题")
    private String examTitle;//问卷标题

//    @Transient
@ApiModelProperty(value = "标准答案")
    private String answer;//标准答案


    @Override
    public void preInsert() {
        if (null == sumScore) {
            sumScore = 0;
        }
        super.preInsert();
    }

}
