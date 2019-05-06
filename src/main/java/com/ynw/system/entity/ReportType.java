package com.ynw.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 举报类型模板
 *  @author ChengZhi
 *  @version 2018-12/7
 */
@Data
@Table(name = "t_sy_report_ctgy")
@ApiModel(value = "ReportType", description = "举报类型模板")
public class ReportType extends DateEntity {

    private static final long serialVersionUID = 1L;

    @Override
    public void setId(String id) {
        this.syReportCtgyId=id;
    }

    @Id
    @ApiModelProperty(value = "举报分类id")
    private String syReportCtgyId;//举报分类id

    @ApiModelProperty(value = "分类名称")
    private String name;//分类名称

    @ApiModelProperty(value = "排序", dataType = "Long", example = "1")
    private Integer sort;//排序

    @ApiModelProperty(value = "添加系统日志的主体")
    @Transient
    private String LogContent;//添加系统日志的主体

    public ReportType(String syReportCtgyId) {
        this.syReportCtgyId = syReportCtgyId;
    }

    public ReportType() {
    }

    @Override
    public void preInsert() {
        if (null == sort) {
            sort = 1;
        }
        super.preInsert();
    }
}
