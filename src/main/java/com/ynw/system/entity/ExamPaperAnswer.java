package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 问卷答案模板
 *  @author ChengZhi
 *  @version 2018-12/15
 */
@Data
@Table(name = "t_kb_exampaper_answer")
@ApiModel(value = "ExamPaperAnswer", description = "问卷答案模板")
public class ExamPaperAnswer extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.kbExampaperAnswerId=id;
    }

    @Id
    @ApiModelProperty(value = "问卷标准答案id")
    private String kbExampaperAnswerId;//问卷标准答案id

    @ApiModelProperty(value = "问卷id")
    private String kbExampaperId;//问卷id

    @ApiModelProperty(value = "题目答案内容")
    private String content;//题目答案内容，一条记录只表示题目的一种答案，多条答案分多条存储。

    @ApiModelProperty(value = "最小分值条件", example = "0")
    private Integer minScore;//最小分值条件

    @ApiModelProperty(value = "最大分值条件", example = "0")
    private Integer maxScore;//最大分值条件

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public ExamPaperAnswer(String kbExampaperAnswerId) {
        this.kbExampaperAnswerId = kbExampaperAnswerId;
    }

    public ExamPaperAnswer() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        if (null == minScore) {
            minScore = 0;
        }
        if (null == maxScore) {
            maxScore = 0;
        }
        super.preInsert();
    }

}
