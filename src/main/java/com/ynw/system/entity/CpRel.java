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
 * 用户cp模型
 *  @author ChengZhi
 *  @version 2019/2/25
 */
@Data
@Table(name = "t_ac_cp_rel")
@ApiModel(value = "CpRel", description = "用户cp模型")
public class CpRel extends DateEntity {

    @Override
    public void setId(String id) {
        this.acCpRelId = id;
    }

    @Id
    @ApiModelProperty(value = "用户cp关系id")
    private String acCpRelId;//用户cp关系id

    @ApiModelProperty(value = "cp关系请求方用户id")
    private String reqUserId;//cp关系请求方用户id

    @ApiModelProperty(value = "cp关系应答方用户id")
    private String resUserId;//cp关系应答方用户id

    @ApiModelProperty(value = "cp关系来源类型")
    private String srcType;//cp关系来源类型，取字典表中定义的key。

    @ApiModelProperty(value = "来源类型关联的id")
    private String srcId;//来源类型关联的id（例如来源于一周情侣活动则存一周情侣的活动登记id）

    @Column(name = "`status`")
    @ApiModelProperty(value = "cp关系状态")
    private String status;//cp关系状态，取字典表中定义的key。

    @Transient
    @ApiModelProperty(value = "请求者姓名")
    private String reqUserName;

    @Transient
    @ApiModelProperty(value = "请求者imid")
    private String reqUserImId;//环信imid

    @Transient
    @ApiModelProperty(value = "被请求者姓名")
    private String resUserName;

    @Transient
    @ApiModelProperty(value = "被请求者imid")
    private String resUserImId;

    @Transient
    @ApiModelProperty(value = "情侣编号")
    private String matchNo;//情侣编号

    @Override
    public void preInsert() {
        super.preInsert();
    }

    public CpRel(String acCpRelId, String reqUserId, String resUserId, String srcType, String srcId, String status) {
        this.acCpRelId = acCpRelId;
        this.reqUserId = reqUserId;
        this.resUserId = resUserId;
        this.srcType = srcType;
        this.srcId = srcId;
        this.status = status;
        preInsert();
    }

    public CpRel(String acCpRelId, String reqUserId, String srcType, String srcId, String status) {
        this.acCpRelId = acCpRelId;
        this.reqUserId = reqUserId;
        this.srcType = srcType;
        this.srcId = srcId;
        this.status = status;
        preInsert();
    }



    public CpRel(String srcId, String status) {
        this.srcId = srcId;
        this.status = status;
    }

    public CpRel() {
    }
}
