package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * 关注模板
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@Data
@ApiModel(value = "Attention", description = "关注模板")
public class Attention extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acAttentionRelId=id;
    }

    @Id
    @ApiModelProperty(value = "用户关注关系id")
    private String acAttentionRelId;//用户关注关系id

    @ApiModelProperty(value = "关注者id")
    private String srcUserId;//关注者id

    @ApiModelProperty(value = "被关注者id")
    private String objUserId;//被关注者id

    @ApiModelProperty(value = "关注者")
    private AcUser srcUser = new AcUser();//关注者

    @ApiModelProperty(value = "被关注者")
    private AcUser objUser = new AcUser();//被关注者

}
