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
 * 糖果总量模板
 *  @author ChengZhi
 *  @version 2019-4-18
 */
@Data
@Table(name = "t_ac_sugar_sum")
@ApiModel(value = "SweetsSum", description = "糖果总量模板")
public class SweetsSum extends DateEntity {

    @Override
    public void setId(String id) {
        this.acSugarSumId = id;
    }

    @Override
    public void preInsert() {
        super.preInsert();
    }

    @Override
    public void preUpdate() {
        super.preUpdate();
        this.updateTime = new Date();
    }

    @Id
    @ApiModelProperty(value = "糖果总量编号")
    private String acSugarSumId;

    @ApiModelProperty(value = "糖果拥有者ID")
    private String acUserId;//糖果拥有者ID

    @Column(name = "`sum`")
    @ApiModelProperty(value = "糖果的总量", dataType = "Long", example = "0")
    private Integer sum;//糖果的总量

    @ApiModelProperty(value = "已经兑换的人民币金额", dataType = "Long", example = "0")
    private Integer goldConversion;//已经兑换的人民币金额

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;//更新时间

    @Transient
    @ApiModelProperty(value = "用户图片")
    private String userImg;

    @Transient
    @ApiModelProperty(value = "用户名称")
    private String userName;

    @Transient
    @ApiModelProperty(value = "用户手机")
    private String userPhone;

    public SweetsSum(String acUserId, Integer sum, Integer goldConversion, Date updateTime) {
        this.acUserId = acUserId;
        this.sum = sum;
        this.goldConversion = goldConversion;
        this.updateTime = updateTime;
    }

    public SweetsSum(String acSugarSumId, Integer sum) {
        this.acSugarSumId = acSugarSumId;
        this.sum = sum;
    }

    public SweetsSum(String acUserId) {
        this.acUserId = acUserId;
    }

    public SweetsSum() {
    }
}
