package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户相片模板
 *  @author ChengZhi
 *  @version 2018-12/11
 */
@Data
@Table(name = "t_ac_u_image")
@ApiModel(value = "AcUserImage", description = "用户相片模板")
public class AcUserImage extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.acUImageId=id;
    }

    @Id
    @ApiModelProperty(value = "用户相片id")
    private String acUImageId;//用户相片id

    @ApiModelProperty(value = "用户id")
    private String acUserId;//用户id

    @ApiModelProperty(value = "用户相片url")
    private String imageUrl;//用户相片url，存url的尾路径，根路径统一配置到系统参数表中。

    @ApiModelProperty(value = "是否设为封面", example = "0")
    private Integer isCover;//是否设为封面（1：是    0：否），一个用户只允许有一张相片被设为封面。

    @ApiModelProperty(value = "状态", example = "0")
    private Integer status;//状态（0:审核中  1：审核通过  -1：审核不通过）

    public AcUserImage(String acUserId) {
        this.acUserId = acUserId;
    }

    public AcUserImage() {
    }
}
