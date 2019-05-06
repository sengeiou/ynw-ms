package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 雷达搜频道模板
 *  @author ChengZhi
 *  @version 2018/3/4
 */
@Data
@Table(name = "t_rs_channel")
@ApiModel(value = "Channel", description = "雷达搜频道模板")
public class Channel extends DateEntity {

    @Override
    public void setId(String id) {
        this.rsChannelId = id;
    }

    @Id
    @ApiModelProperty(value = "雷达搜频道Id")
    private String rsChannelId;

    @Column(name = "name")
    @ApiModelProperty(value = "频道名")
    private String name;//频道名

    @Transient
    @ApiModelProperty(value = "日志主体")
    private String LogContent;//日志主体

    public Channel(String rsChannelId) {
        this.rsChannelId = rsChannelId;
    }

    public Channel() {
    }
}
