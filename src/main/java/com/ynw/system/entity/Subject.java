package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 题目模板
 *  @author ChengZhi
 *  @version 2018-12/15
 */
@Data
@Table(name = "t_kb_subject")
@ApiModel(value = "Subject", description = "题目模板")
public class Subject extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.kbSubjectId=id;
    }

    @Id
    @ApiModelProperty(value = "题目id")
    private String kbSubjectId;//题目id

    @ApiModelProperty(value = "题目内容")
    private String content;//题目内容

    @ApiModelProperty(value = "题目类型")
    private String type;//题目类型，目前只支持单选题，取字典表中定义的key。

    @ApiModelProperty(value = "排序",dataType = "Long", example = "1")
    private Integer sort;//排序

    @ApiModelProperty(value = "题目的路径类型")
    private String pathType;//题目的路径类型，当问卷是按路径选择计算答案时，此处标识该道题在整个问卷中的路径位置，取字典表中定义的key。

    @ApiModelProperty(value = "问卷id")
    private String kbExampaperId;//问卷id

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    @Transient
    @ApiModelProperty(value = "题目选项集合")
    private List<SubjectItem> subjectItemList = new ArrayList<>();//题目选项集合

    public Subject(String kbSubjectId) {
        this.kbSubjectId = kbSubjectId;
    }

    public Subject() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }
}
