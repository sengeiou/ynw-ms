package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 雷达搜题目选项模板
 *  @author ChengZhi
 *  @version 2018/3/4
 */
@Data
@Table(name = "t_rs_subject_item")
@ApiModel(value = "RadarSubjectItem", description = "雷达搜题目选项模板")
public class RadarSubjectItem extends DateEntity {

    @Override
    public void setId(String id) {
        this.rsSubjectItemId = id;
    }

    @Id
    @ApiModelProperty(value = "雷达搜用户题目选项id")
    private String rsSubjectItemId;//雷达搜用户题目选项id

    @ApiModelProperty(value = "雷达搜用户题目id")
    private String rsSubjectId;//雷达搜用户题目id

    @Column(name = "content")
    @ApiModelProperty(value = "题目选项内容")
    private String content;//题目选项内容

    @Column(name = "sort")
    @ApiModelProperty(value = "排序", dataType = "Long", example = "1")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "日志主体")
    private String LogContent;//日志主体

    @Override
    public void preInsert() {
        reuse();
        super.preInsert();
    }

    /**
     *  复用
     */
    private void reuse() {
        if (null == this.sort)
            this.sort = 1;
    }

    @Override
    public void preUpdate() {
        reuse();
        super.preUpdate();
    }

    public RadarSubjectItem(String rsSubjectItemId) {
        this.rsSubjectItemId = rsSubjectItemId;
    }

    public RadarSubjectItem() {
    }
}
