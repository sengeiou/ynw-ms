package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.annotations.Options;

import javax.persistence.Id;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户cp流水模型
 *  @author ChengZhi
 *  @version 2019/2/25
 */
@Data
@Table(name = "t_ac_cp_flow")
@ApiModel(value = "CpFlow", description = "用户cp流水模型")
public class CpFlow extends DateEntity {

    @Override
    public void setId(String id) {
        this.acCpFlowId = id;
    }

    @Id
    @ApiModelProperty(value = "用户cp关系流水id")
    private String acCpFlowId;//用户cp关系流水id

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

    @ApiModelProperty(value = "cp关系状态")
    private String status;//cp关系状态，取字典表中定义的key。

    @Override
    public void preInsert() {
        super.preInsert();
    }

    @Override
    public void beforeInsert() {
        super.beforeInsert();
    }

    public CpFlow(String acCpFlowId, String acCpRelId, String reqUserId, String resUserId, String srcType, String srcId, String status) {
        this.acCpFlowId = acCpFlowId;
        this.acCpRelId = acCpRelId;
        this.reqUserId = reqUserId;
        this.resUserId = resUserId;
        this.srcType = srcType;
        this.srcId = srcId;
        this.status = status;
        beforeInsert();
    }

    public CpFlow(String acCpFlowId, String acCpRelId, String reqUserId, String srcType, String srcId, String status) {
        this.acCpFlowId = acCpFlowId;
        this.acCpRelId = acCpRelId;
        this.reqUserId = reqUserId;
        this.srcType = srcType;
        this.srcId = srcId;
        this.status = status;
        preInsert();
    }

    public CpFlow() {
    }
}
