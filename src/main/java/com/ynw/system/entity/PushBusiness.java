package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 推送消息业务模板
 *  @author ChengZhi
 *  @version 2018/3/5
 */
@Data
@Table(name = "t_mg_business")
@ApiModel(value = "PushBusiness", description = "推送消息业务模板")
public class PushBusiness implements Entity {

    @Override
    public void setId(String id) {
        this.mgBusinessId = id;
    }

    @Id
    @ApiModelProperty(value = "消息业务id")
    private String mgBusinessId;//消息业务id

    @ApiModelProperty(value = "消息体id")
    private String mgBodyId;//消息体id

    @ApiModelProperty(value = "业务类型")
    private String busiType;//业务类型，取字典表中定义的key。

    @Column(name = "asso_info_1")
    @ApiModelProperty(value = "业务关联信息1")
    private String infoOne;//业务关联信息1

    @Column(name = "asso_info_2")
    @ApiModelProperty(value = "业务关联信息2")
    private String infoTwo;//业务关联信息2

    public PushBusiness(String mgBodyId, String busiType, String infoOne, String infoTwo) {
        this.mgBodyId = mgBodyId;
        this.busiType = busiType;
        this.infoOne = infoOne;
        this.infoTwo = infoTwo;
    }

    public PushBusiness() {
    }
}
