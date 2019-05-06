package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 约活动模板
 * @author ChengZhi
 * @version 2019/4/25
 */
@Data
@Table(name = "t_at_register")
@ApiModel(value = "Engagement", description = "约活动模板")
public class Engagement extends Register {

    @ApiModelProperty(value = "活动地点省份id")
    private String addrProvinceId;//活动地点省份id

    @ApiModelProperty(value = "活动地点城市id")
    private String addrCityId;//活动地点城市id

    @ApiModelProperty(value = "活动地点区县id")
    private String addrDistrictId;//活动地点区县id

    @ApiModelProperty(value = "活动地点详细地点")
    private String addrDetail;//活动地点详细地点

    @ApiModelProperty(value = "活动单人费用", example = "0.0")
    private Double singleFee;//活动单人费用（单位：元）

    @ApiModelProperty(value = "活动最少人数要求", example = "0")
    private Integer minNumber;//活动最少人数要求（0表示不限，无要求）

    @ApiModelProperty(value = "活动最多人数要求", example = "0")
    private Integer maxNumber;//活动最多人数要求（0表示不限，无要求）

    @ApiModelProperty(value = "约活动主题id")
    private String atInviteThemeId;//约活动主题id

    @ApiModelProperty(value = "限制什么性别可以参与", example = "2")
    private Integer limitSex;//限制什么性别可以参与 0:男 1:女 2:都可以

    @ApiModelProperty(value = "审核状态", example = "0")
    private Integer checkStatus;//审核状态（-1审核不通过  0 审核中  1审核通过）

    @ApiModelProperty(value = "收费状态", example = "2")
    private Integer feeStatus;//收费状态（ 0:AA 1 :自定义 2:发起人自费）

    @ApiModelProperty(value = "主题名")
    @Transient
    private String themeName;//主题名

    @Transient
    @ApiModelProperty(value = "发起人电话")
    private String userPhone;//发起人电话

    @Transient
    @ApiModelProperty(value = "报名人数", example = "10")
    private Integer userNum;//报名人数

    @Transient
    @ApiModelProperty(value = "发起人姓名")
    private String userName;//发起人姓名

    @Override
    public String getLogContent() {
        return super.getLogContent();
    }

    @Override
    public void setLogContent(String LogContent) {
        super.setLogContent(LogContent);
    }

    @Override
    public String toString() {
        return "Engagement{" +
                "addrProvinceId='" + addrProvinceId + '\'' +
                ", addrCityId='" + addrCityId + '\'' +
                ", addrDistrictId='" + addrDistrictId + '\'' +
                ", addrDetail='" + addrDetail + '\'' +
                ", singleFee=" + singleFee +
                ", minNumber=" + minNumber +
                ", maxNumber=" + maxNumber +
                ", atInviteThemeId='" + atInviteThemeId + '\'' +
                ", limitSex=" + limitSex +
                ", checkStatus=" + checkStatus +
                ", feeStatus=" + feeStatus +
                ", themeName='" + themeName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userNum=" + userNum +
                ", userName='" + userName + '\'' +
                ", LogContent='" + getLogContent() + '\'' +
                '}';
    }

    public Engagement(String atRegisterId) {
        super(atRegisterId);
    }

    public Engagement() {
    }
}
