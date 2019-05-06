package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 活动打卡任务模板
 *  @author ChengZhi
 *  @version 2019/2/27
 */
@Data
@Table(name = "t_at_wkcp_task")
@ApiModel(value = "ActiveTaskClock", description = "活动打卡任务模板")
public class ActiveTaskClock extends DateEntity {

    @Override
    public void setId(String id) {
        this.atWkcpTaskId = id;
    }

    @Id
    @ApiModelProperty(value = "活动任务id")
    private String atWkcpTaskId;//一周情侣活动任务id

    @ApiModelProperty(value = "活动登记id")
    private String atRegisterId;//活动登记id

    @ApiModelProperty(value = "活动第几天", example = "1")
    private Integer day;//活动第几天（从第1天到第7天）

    @ApiModelProperty(value = "任务描述")
    private String taskDes;//任务描述

    @Transient
    @ApiModelProperty(value = "日志主体")
    private String LogContent;//日志主体

    public ActiveTaskClock(String atWkcpTaskId) {
        this.atWkcpTaskId = atWkcpTaskId;
    }

    public ActiveTaskClock(String atRegisterId, Integer day) {
        this.atRegisterId = atRegisterId;
        this.day = day;
    }

    public ActiveTaskClock() {
    }

    @Override
    public void preInsert() {
        if (null == day)
            day = 1;
        super.preInsert();
    }

    @Override
    public void preUpdate() {
        if (null == day)
            day = 1;
        super.preUpdate();
    }
}
