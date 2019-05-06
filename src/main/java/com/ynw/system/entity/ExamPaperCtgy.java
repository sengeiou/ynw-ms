package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 问卷分类模板
 *  @author ChengZhi
 *  @version 2018-12/13
 */
@Data
@Table(name = "t_kb_exampaper_ctgy")
@ApiModel(value = "ExamPaperCtgy", description = "问卷分类模板")
public class ExamPaperCtgy extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.kbExampaperCtgyId=id;
    }

    @Id
    @ApiModelProperty(value = "问卷分类id")
    private String kbExampaperCtgyId;//问卷分类id

    @ApiModelProperty(value = "分类名称")
    private String name;//分类名称

    @ApiModelProperty(value = "排序", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public ExamPaperCtgy() {
    }

    public ExamPaperCtgy(String kbExampaperCtgyId) {
        this.kbExampaperCtgyId = kbExampaperCtgyId;
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }

}
