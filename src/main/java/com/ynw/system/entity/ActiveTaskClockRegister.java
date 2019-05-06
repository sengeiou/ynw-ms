package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 活动打卡登记模板
 *  @author ChengZhi
 *  @version 2019/2/27
 */
@ApiModel(value = "ActiveTaskClockRegister", description = "活动打卡登记模板")
@Data
@Table(name = "t_at_wkcp_do_task")
public class ActiveTaskClockRegister extends DateEntity {

    @Override
    public void setId(String id) {
        this.atWkcpDoTaskId = id;
    }

    @Id
    @ApiModelProperty(value = "打卡记录id")
    private String atWkcpDoTaskId;//一周情侣活动用户打卡记录id

    @ApiModelProperty(value = "任务id")
    private String atWkcpTaskId;//一周情侣活动任务id

    @ApiModelProperty(value = "cp关系id")
    private String acCpRelId;//用户cp关系id

    @ApiModelProperty(value = "动态id")
    private String dsMoodId;//动态id，即用户cp打卡发布的那条动态id。

    @ApiModelProperty(value = "状态",example = "1")
    @Column(name = "`status`")
    private Integer status;//状态（0：等待cp方同意   1：正常发布   -1：cp不同意，取消发布）

    @Transient
    @ApiModelProperty(value = "cp请求者姓名")
    private String reqName;//cp请求者姓名

    @Transient
    @ApiModelProperty(value = "cp接受者姓名")
    private String resName;//cp接受者姓名

    @Transient
    @ApiModelProperty(value = "匹配编号")
    private String matchNo;//匹配编号

    @Transient
    @ApiModelProperty(value = "任务简介")
    private String missionName;//任务简介

    @Transient
    @ApiModelProperty(value = "任务第几天", example = "1")//数字类型需要加默认值，否则会报空字符串
    private Integer missionDay;//任务第几天

    @Transient
    @ApiModelProperty(value = "活动编号")
    private String atRegisterId;//活动编号

}
