package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 问卷模板
 *  @author ChengZhi
 *  @version 2018-12/13
 */
@Data
@Table(name = "t_kb_exampaper")
@ApiModel(value = "ExamPaper", description = "问卷模板")
public class ExamPaper extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.kbExampaperId=id;
    }

    @Id
    @ApiModelProperty(value = "问卷id")
    private String kbExampaperId;//问卷id

    @ApiModelProperty(value = "问卷分类id")
    private String kbExampaperCtgyId;//问卷分类id

    @ApiModelProperty(value = "问卷标题")
    private String title;//问卷标题

    @ApiModelProperty(value = "问卷类型")
    private String type;//问卷类型，指定了该问卷计算答案的方式，取字典表中定义的key。

    @ApiModelProperty(value = "状态", example = "1")
    private Integer status;//状态（1：有效   0：无效）

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @ApiModelProperty(value = "问卷分类名字")
    @Transient
    private String examPaperCtgyName;//问卷分类名字

    @Transient
    @ApiModelProperty(value = "举问卷类型名字")
    private String typeName;//举问卷类型名字

    @Transient
    @ApiModelProperty(value = "题目数量", example = "1")
    private Integer examNum;//题目数量

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public ExamPaper(String kbExampaperId) {
        this.kbExampaperId = kbExampaperId;
    }

    public ExamPaper() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        if (null == status) {
            status = 1;
        }
        super.preInsert();
    }

}
