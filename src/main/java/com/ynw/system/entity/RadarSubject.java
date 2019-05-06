package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 雷达搜题目模板
 *  @author ChengZhi
 *  @version 2018/3/2
 */
@Data
@Table(name = "t_rs_subject")
@ApiModel(value = "RadarSubject", description = "雷达搜题目模板")
public class RadarSubject extends DateEntity {

    @Override
    public void setId(String id) {
        this.rsSubjectId = id;
    }

    @Id
    @ApiModelProperty(value = "雷达搜用户题目id")
    private String rsSubjectId;//雷达搜用户题目id

    @Column(name = "`content`")
    @ApiModelProperty(value = "题目内容")
    private String content;//题目内容

    @Column(name = "`type`")
    @ApiModelProperty(value = "题目类型")
    private String type;//题目类型，目前只支持单选题，取字典表中定义的key。

    @ApiModelProperty(value = "题目出处", dataType = "Long", example = "0")
    private Integer fromType;//题目出处（0：系统    1：用户自定义）

    @ApiModelProperty(value = "自定义题目的用户id")
    private String acUserId;//自定义题目的用户id

    @ApiModelProperty(value = "适用范围", dataType = "Long", example = "2")
    private Integer applied;//适用范围（0：适用男生回答   1：适用女生回答   2：男女生都适用）

    @ApiModelProperty(value = "排序",dataType = "Long", example = "1")
    @Column(name = "`sort`")
    private Integer sort;//排序

    @Transient
    @ApiModelProperty(value = "日志主体")
    private String LogContent;//日志主体

    @Transient
    @ApiModelProperty(value = "题目类型")
    private String typeName;//题目类型

    @Transient
    @ApiModelProperty(value = "出题人")
    private String userName;//出题人

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
        if (null == this.fromType)
            this.fromType = 0;
        if (null == this.applied)
            this.applied = 2;
    }

    @Override
    public void preUpdate() {
        reuse();
        super.preUpdate();
    }

    public RadarSubject(String rsSubjectId) {
        this.rsSubjectId = rsSubjectId;
    }

    public RadarSubject() {
    }
}
