package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 雷达搜短语模板
 *  @author ChengZhi
 *  @version 2018/3/4
 */
@Data
@Table(name = "t_rs_phrase")
@ApiModel(value = "Phrase", description = "雷达搜短语模板")
public class Phrase extends DateEntity {

    @Override
    public void setId(String id) {
        this.rsPhraseId = id;
    }

    @Id
    @ApiModelProperty(value = "短语编号")
    private String rsPhraseId;

    @Column(name = "content")
    @ApiModelProperty(value = "短语内容")
    private String content;//短语内容

    @Transient
    @ApiModelProperty(value = "日志主体")
    private String LogContent;//日志主体

    public Phrase(String rsPhraseId) {
        this.rsPhraseId = rsPhraseId;
    }

    public Phrase() {
    }
}
