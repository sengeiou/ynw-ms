package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 糖果流水模板
 *  @author ChengZhi
 *  @version 2019-4-17
 */
@Data
@Table(name = "t_ac_sugar_flow")
@ApiModel(value = "SweetsFlow", description = "糖果流水模板")
public class SweetsFlow extends DateEntity {

    @Override
    public void setId(String id) {
        this.acSugarFlowId = id;
    }

    @Override
    public void preInsert() {
        super.preInsert();
    }

    public void preInserts(String acUserId,String assoBusinessId) {
        this.acUserId=acUserId;
        this.assoBusinessId=assoBusinessId;
        this.assoBusinessKey="INVITE_PEOPLE";
        //邀请一定是收入
        this.type=0;
        this.updateTime=new Date();
        this.quantity=388;
        preInsert();
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        this.updateTime = new Date();
    }

    @Id
    @ApiModelProperty(value = "糖果流水编号")
    private String acSugarFlowId;

    @ApiModelProperty(value = "糖果拥有者ID")
    private String acUserId;//糖果拥有者ID

    @Column(name = "`quantity`")
    @ApiModelProperty(value = "当次糖果的获得量或者消耗量", dataType = "Long", example = "0")
    private Integer quantity;//当次糖果的获得量或者消耗量

    @Column(name = "`type`")
    @ApiModelProperty(value = "糖果的消耗类型", dataType = "Long", example = "0")
    private Integer type;//糖果的消耗类型 1:支出,0:收入

    @ApiModelProperty(value = "来源id")
    private String assoBusinessId;//

    @ApiModelProperty(value = "取字典表中的key")
    private String assoBusinessKey;//取字典表中的key,表示不同的糖果来源

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;//更新时间

    @ApiModelProperty(value = "支付宝账号")
    private String alipayAccount;//支付宝账号

    @ApiModelProperty(value = "支付宝二维码的图片路径")
    private String alipayCode;//支付宝二维码的图片路径

    @ApiModelProperty(value = "用户真实姓名")
    private String realName;//用户真实姓名

    @Transient
    @ApiModelProperty(value = "拥有糖果总数", dataType = "Long", example = "0")
    private Integer sum;//拥有糖果总数

    @Transient
    @ApiModelProperty(value = "用户图片")
    private String userImg;

    @Transient
    @ApiModelProperty(value = "用户名")
    private String userName;

    @Transient
    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @Transient
    @ApiModelProperty(value = "糖果来源key的值")
    private String businessKeyValue;

    @Transient
    @ApiModelProperty(value = "添加系统日志的主体")
    private String LogContent;//添加系统日志的主体

}
