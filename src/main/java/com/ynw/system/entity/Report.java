package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * 举报模板
 *  @author ChengZhi
 *  @version 2018-12/7
 */
@Data
@Table(name = "t_sy_report")
@ApiModel(value = "Report", description = "举报模板")
public class Report extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.syReportId=id;
    }

    @Id
    @ApiModelProperty(value = "举报id")
    private String syReportId;//举报id

    @ApiModelProperty(value = "举报人id")
    private String acUserId;//举报人id

    @ApiModelProperty(value = "举报分类id")
    private String syReportCtgyId;//举报分类id

    @ApiModelProperty(value = "举报对象的业务类型")
    private String targetType;//举报对象的业务类型，取字典表中定义的key。

    @ApiModelProperty(value = "举报对象的业务id")
    private String targetId;//举报对象的业务id

    @ApiModelProperty(value = "举报理由的详细内容")
    private String content;//举报理由的详细内容

    @ApiModelProperty(value = "举报分类名字")
    @Transient
    private String reportCtgyName;//举报分类名字

    @Transient
    @ApiModelProperty(value = "业务类型名字")
    private String targetTypeName;//业务类型名字

    @Transient
    @ApiModelProperty(value = "举报人电话")
    private String acUserPhone;//举报人电话

    @Transient
    @ApiModelProperty(value = "举报人昵称")
    private String acUserName;//举报人昵称

    @Transient
    @ApiModelProperty(value = "举报对象")
    private Mood mood = new Mood();//举报对象

    @Transient
    @ApiModelProperty(value = "举报对象的举报数", dataType = "Long", example = "0")
    private Integer reportNumber;//举报对象的举报数

    @Transient
    @ApiModelProperty(value = "举报对象状态",dataType = "Long",example = "1")
    private Integer status;//举报对象状态

}
