package com.ynw.system.controller;

import com.ynw.system.entity.ParaConf;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ParaConfService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/5
 */
@RestController
@RequestMapping("/ynw-ms/paraConf")
@Api(tags = "系统参数接口",description = "系统参数查询及操作接口")
public class ParaConfController {

    @Autowired
    private ParaConfService paraConfService;

    public static ParaConfController paraConfController;
    //调用service层初始化(防止外部调用时报空)
    @PostConstruct
    public void init() {
        paraConfController = this;
        paraConfController.paraConfController = this.paraConfController;
    }

    /**
     *  根据键key获取系统路径
     * @param key
     * @return
     */
    public String findUrlByKey(String key) {
        if (StringUtils.isNotEmpty(key)) {
            ParaConf paraConf = paraConfService.findByKey(key);
            if (null != paraConf) {
                return paraConf.getValue();
            }
        }
        return "";
    }

    /**
     *  获取所有
     * @return
     */
    public List<ParaConf> findAll() {
        return paraConfService.selectAll();
    }

    /**
     *  条件查询系统参数配置
     * @param paraConf
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryParaConf")
    @ApiOperation(value = "条件查询系统参数配置", notes = "传入：页码（nowPage必传），键（key），值（value）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryParaConf(ParaConf paraConf, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<ParaConf> paraConfList = paraConfService.conditionQueryParaConf(paraConf, start, pageSize);
            if (paraConfList.size() > 0) {
                return ResultUtil.successResponse(paraConfList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询系统参数配置
     * @param paraConf
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询系统参数配置", notes = "传入：键（key），值（value）")
    public ResponseEntity<Result> count(ParaConf paraConf) {
        Integer count = paraConfService.count(paraConf);
        return ResultUtil.successResponse(count);
    }

    /**
     *  添加系统参数配置
     * @param paraConf
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加系统参数配置", notes = "传入：描述（describe），键（key），" +
            "值（value）,添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(ParaConf paraConf) {
        if (StringUtils.isNotEmpty(paraConf.getDescribe()) && StringUtils.isNotEmpty(paraConf.getKey())
                && StringUtils.isNotEmpty(paraConf.getValue())) {
            if (paraConfService.insert(paraConf) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改系统参数配置
     * @param paraConf
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改系统参数配置", notes = "传入：系统参数配置ID(syParaConfId),描述（describe），键（key），" +
            "值（value）,添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(ParaConf paraConf) {
        if (StringUtils.isNotEmpty(paraConf.getDescribe()) && StringUtils.isNotEmpty(paraConf.getKey())
                && StringUtils.isNotEmpty(paraConf.getValue()) && StringUtils.isNotEmpty(paraConf.getSyParaConfId())) {
            ParaConf conf = paraConfService.selectOne(new ParaConf(paraConf.getSyParaConfId()));
            if (null != conf) {
                conf.setKey(paraConf.getKey());
                conf.setDescribe(paraConf.getDescribe());
                conf.setValue(paraConf.getValue());
                if (paraConfService.update(conf) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改系统参数配置
     * @param paraConf
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "修改系统参数配置", notes = "传入：系统参数配置ID(syParaConfId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(ParaConf paraConf) {
        if (StringUtils.isNotEmpty(paraConf.getSyParaConfId())) {
            if (paraConfService.delete(paraConf) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
