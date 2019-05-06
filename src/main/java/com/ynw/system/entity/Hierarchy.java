package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 用户等级模板
 *  @author ChengZhi
 *  @version 2018/2/28
 */
@Data
@Table(name = "t_ac_u_level")
@ApiModel(value = "Hierarchy", description = "用户等级模板")
public class Hierarchy extends DateEntity {

    @Override
    public void setId(String id) {
        this.acULevelId = id;
    }

    @Id
    @ApiModelProperty(value = "用户等级id")
    private String acULevelId;//用户等级id

    @Column(name = "`no`")
    @ApiModelProperty(value = "等级编号", example = "0")
    private Integer no;//等级编号，最低等级为0，往上递增

    @Column(name = "`name`")
    @ApiModelProperty(value = "等级名称")
    private String name;//等级名称

    @ApiModelProperty(value = "要求在等级有效期内累积获取到的最少情豆数量", example = "0")
    private Integer needGetBean;//要求在等级有效期内累积获取到的最少情豆数量

    @ApiModelProperty(value = "要求在等级有效期内累积支出的最少情豆数量", example = "0")
    private Integer needPayoutBean;//要求在等级有效期内累积支出的最少情豆数量

    @Transient
    @ApiModelProperty(value = "日志主体")
    private String LogContent;//日志主体

    @Override
    public void preInsert() {
        assignment();
        super.preInsert();
    }

    /**
     *  赋值
     */
    public void assignment() {
        if (null == this.no)
            this.no = 0;
        if (null == this.needGetBean)
            this.needGetBean = 0;
        if (null == this.needPayoutBean)
            this.needPayoutBean = 0;
    }

    @Override
    public void preUpdate() {
        assignment();
        super.preUpdate();
    }

    public Hierarchy(String acULevelId) {
        this.acULevelId = acULevelId;
    }

    public Hierarchy() {
    }
}
