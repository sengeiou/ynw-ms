package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 问卷测试流水模板
 *  @author ChengZhi
 *  @version 2018-12/18
 */
@Data
@ApiModel(value = "ExamPaperAnswerFlow", description = "问卷测试流水模板")
public class ExamPaperAnswerFlow extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.kbTestAnswerFlowId=id;
    }

    @Id
    @ApiModelProperty(value = "测试答题流水id")
    private String kbTestAnswerFlowId;//测试答题流水id

    @ApiModelProperty(value = "问卷测试id")
    private String kbExampaperTestId;//问卷测试id

    @ApiModelProperty(value = "题目id")
    private String kbSubjectId;//题目id

    @ApiModelProperty(value = "题目选项id")
    private String kbSubjectItemId;//题目选项id

    @ApiModelProperty(value = "题目标题")
    private String subjectName;//题目标题

    @ApiModelProperty(value = "题目选项内容")
    private String subjectItemName;//题目选项内容

}
