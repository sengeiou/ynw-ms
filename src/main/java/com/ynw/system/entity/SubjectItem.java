package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 题目选项模板
 *  @author ChengZhi
 *  @version 2018-12/15
 */
@Data
@Table(name = "t_kb_subject_item")
@ApiModel(value = "SubjectItem", description = "题目选项模板")
public class SubjectItem extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.kbSubjectItemId=id;
    }

    @Id
    @ApiModelProperty(value = "题目选项id")
    private String kbSubjectItemId;//题目选项id

    @ApiModelProperty(value = "题目id")
    private String kbSubjectId;//题目id

    @ApiModelProperty(value = "题目选项内容")
    private String content;//题目选项内容

    @ApiModelProperty(value = "排序", dataType = "Long", example = "1")
    private Integer sort;//排序

    @ApiModelProperty(value = "是否是路径的终结点", dataType = "Long", example = "0")
    private Integer isPathEnd;//是否是路径的终结点，当问卷是按选择的路径计算答案时，此处标识该选项是否是路径选择的终结点。（1：是    0：否）

    @ApiModelProperty(value = "问卷标准答案id")
    private String kbExampaperAnswerId;//问卷标准答案id

    @ApiModelProperty(value = "下一题id")
    private String nextSubjectId;//下一题id，当问卷是按答题路径计算答案时，此处提供选中该选项后的下一题的路径。

    @ApiModelProperty(value = "分值", dataType = "Long", example = "0")
    private Integer score;//分值，当问卷是按分值计算答案时，此处提供选项选中时的分值。

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

    public SubjectItem(String kbSubjectItemId) {
        this.kbSubjectItemId = kbSubjectItemId;
    }

    public SubjectItem() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        if (null == isPathEnd) {
            isPathEnd = 0;
        }
        if (null == score) {
            score = 0;
        }
        super.preInsert();
    }
}
