package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 隐私消息模板
 *  @author ChengZhi
 *  @version 2018-12/13
 */
@Data
@Table(name = "t_ac_s_privacy")
@ApiModel(value = "Privacy", description = "隐私消息模板")
public class Privacy extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acSPrivacyId=id;
    }

    @Override
    public void preInsert() {
        if (null == this.pSearchFlag)
            this.pSearchFlag = 1;
        if (null == this.noSearchFlag)
            this.noSearchFlag = 1;
        if (null == this.cCommendFlag)
            this.cCommendFlag = 1;
        super.preInsert();
    }

    @Id
    @ApiModelProperty(value = "隐私设置id")
    private String acSPrivacyId;//隐私设置id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "手机号搜索标识", example = "1", dataType = "Long")
    private Integer pSearchFlag;//手机号搜索标识，是否能通过手机号搜索到我（1：是    0：否）

    @ApiModelProperty(value = "ID号搜索标识", example = "1", dataType = "Long")
    private Integer noSearchFlag;//ID号搜索标识，是否能通过ID号搜索到我（1：是    0：否）

    @ApiModelProperty(value = "通讯录好友推荐标识", example = "1", dataType = "Long")
    private Integer cCommendFlag;//通讯录好友推荐标识，是否向我推荐通讯录好友（1：是   0：否）

    public Privacy(String acUserId) {
        this.acUserId = acUserId;
    }

    public Privacy() {
    }
}
