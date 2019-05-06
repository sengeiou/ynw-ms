package com.ynw.system.controller;

import com.ynw.system.entity.Report;
import com.ynw.system.entity.ReportType;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ReportService;
import com.ynw.system.service.ReportTypeService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/7
 */
@RestController
@RequestMapping("/ynw-ms/report")
@Api(tags = "举报信息接口",description = "举报信息查询及操作接口")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportTypeService reportTypeService;

    /**
     *  条件查询举报信息
     * @param report
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryReport")
    @ApiOperation(value = "条件查询举报信息", notes = "传入：页码（nowPage必传），举报人昵称（acUserName），举报人电话（acUserPhone）" +
            "，举报分类id（syReportCtgyId），举报对象的业务类型（targetType），举报对象状态（status）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryReport(Report report, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Report> reportList = reportService.conditionQueryReport(report, start, pageSize);
            if (reportList.size() > 0) {
                return ResultUtil.successResponse(reportList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总数
     * @param report
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询举报信息总数", notes = "传入：举报人昵称（acUserName），举报人电话（acUserPhone）" +
            "，举报分类id（syReportCtgyId），举报对象的业务类型（targetType），举报对象状态（status）")
    public ResponseEntity<Result> count(Report report) {
        return ResultUtil.successResponse(reportService.count(report));
    }

    /**
     *  查询举报对象数据
     * @param nowPage
     * @return
     */
    @PostMapping("/findReportTarget")
    @ApiOperation(value = "查询举报对象数据", notes = "传入：当前页（nowPage），举报对象昵称（name），举报对象电话（phone）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "name", value = "举报对象昵称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "举报对象电话", dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> findReportTarget(Integer nowPage, String name, String phone) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Report> reportList = reportService.findReportTarget(start, pageSize, name, phone);
            if (reportList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                for (Report report: reportList
                     ) {
                    report.getMood().setUserImage(url + report.getMood().getUserImage());
                }
                return ResultUtil.successResponse(reportList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总数
     * @return
     */
    @PostMapping("/countReportTarget")
    @ApiOperation(value = "条件查询举报对象总数", notes = "传入：举报对象昵称（name），举报对象电话（phone）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1"),
            @ApiImplicitParam(name = "name", value = "举报对象昵称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "phone", value = "举报对象电话", dataType = "String", paramType = "query"),
    })
    public ResponseEntity<Result> countReportTarget(String name, String phone) {
        return ResultUtil.successResponse(reportService.countReportTarget(name, phone));
    }

    /**
     *  根据举报对象编号查询数据
     * @param targetId
     * @return
     */
    @PostMapping("/findReportByTarget")
    @ApiOperation(value = "根据举报对象编号查询数据", notes = "传入：举报对象编号（targetId）")
    @ApiImplicitParam(name = "targetId", value = "举报对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findReportByTarget(String targetId) {
        if (StringUtils.isNotEmpty(targetId)) {
            List<Report> reportList = reportService.findReportByTarget(targetId);
            if (reportList.size() > 0) {
                return ResultUtil.successResponse(reportList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  查询所有举报类型
     * @return
     */
    @PostMapping("/findReportTypeAll")
    @ApiOperation(value = "查询所有举报类型")
    public ResponseEntity<Result> findReportTypeAll() {
        List<ReportType> reportTypeList = reportTypeService.findAll();
        if (reportTypeList.size() > 0) {
            return ResultUtil.successResponse(reportTypeList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加举报类型
     * @param reportType
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加举报类型", notes = "传入：分类名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(ReportType reportType) {
        if (StringUtils.isNotEmpty(reportType.getName())) {
            List<ReportType> reportTypeList = reportTypeService.findBySortMax();
            if (reportTypeList.size() > 0) {
                if (null != reportTypeList.get(0).getSort()) {
                    //数据库有数据并且最大的sort不为空
                    reportType.setSort(reportTypeList.get(0).getSort() + 1);
                }
            }
            if (reportTypeService.insert(reportType) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新举报类型
     * @param reportType
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新举报类型", notes = "传入：举报分类id(syReportCtgyId),分类名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(ReportType reportType) {
        if (StringUtils.isNotEmpty(reportType.getName()) && StringUtils.isNotEmpty(reportType.getSyReportCtgyId())) {
            ReportType reportType1 = reportTypeService.selectOne(new ReportType(reportType.getSyReportCtgyId()));
            if (null != reportType1) {
                reportType1.setName(reportType.getName());
                if (reportTypeService.update(reportType1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除举报类型
     * @param reportType
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "更新举报类型", notes = "传入：举报分类id(syReportCtgyId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(ReportType reportType) {
        if (StringUtils.isNotEmpty(reportType.getSyReportCtgyId())) {
            if (reportTypeService.delete(reportType) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param syReportCtgyId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（syReportCtgyId)(必传)")
    @ApiImplicitParam(name = "syReportCtgyId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String syReportCtgyId) {
        return move(syReportCtgyId, 1);
    }

    /**
     *  下移
     * @param syReportCtgyId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（syReportCtgyId)(必传)")
    @ApiImplicitParam(name = "syReportCtgyId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String syReportCtgyId) {
        return move(syReportCtgyId, -1);
    }

    /**
     *  上移下移复用
     * @param syReportCtgyId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String syReportCtgyId, Integer move) {
        if (StringUtils.isNotEmpty(syReportCtgyId)) {
            List<ReportType> reportTypeList = reportTypeService.findBySortMax();
            if (null == reportTypeList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (reportTypeList.size() > 1) {
                Integer code = reportTypeService.move(syReportCtgyId, reportTypeList, move);
                if (code > 0) {
                    if (code == 14) {
                        return ResultUtil.errorResponse(ResultEnums.MOVE_UP_ERROR);
                    } else if (code == 15) {
                        return ResultUtil.errorResponse(ResultEnums.MOVE_DOWN_ERROR);
                    } else {
                        return ResultUtil.successResponse();
                    }
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
