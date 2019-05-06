package com.ynw.system.controller;

import com.ynw.system.entity.RegisterClassify;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.RegisterClassifyService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ynw-ms/register-classify")
@Api(tags = "活动分类接口",description = "活动分类查询及操作接口")
public class RegisterClassifyController {

    @Autowired
    private RegisterClassifyService registerClassifyService;

    /**
     *  获取排序后所有分类信息
     * @return
     */
    @PostMapping("/conditionQueryRegisterClassify")
    @ApiOperation(value = "获取排序后所有分类信息")
    public ResponseEntity<Result> conditionQueryRegisterClassify() {
        List<RegisterClassify> classifyList = registerClassifyService.conditionQueryRegisterClassify();
        if (classifyList.size() > 0) {
            return ResultUtil.successResponse(classifyList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加分类
     * @param registerClassify
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加活动分类", notes = "传入：活动分类名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(RegisterClassify registerClassify) {
        if (StringUtils.isNotEmpty(registerClassify.getName())) {
            if (registerClassifyService.insert(registerClassify) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除分类
     * @param classify
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除分类", notes = "传入：活动分类id（atCtgyId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(RegisterClassify classify) {
        if (StringUtils.isNotEmpty(classify.getAtCtgyId())) {
            if (registerClassifyService.delete(classify) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新分类
     * @param registerClassify
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新分类", notes = "传入：活动分类id（atCtgyId），活动分类名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(RegisterClassify registerClassify) {
        if (StringUtils.isNotEmpty(registerClassify.getAtCtgyId()) && StringUtils.isNotEmpty(registerClassify.getName())) {
            RegisterClassify classify = registerClassifyService.selectOne(new RegisterClassify(registerClassify.getAtCtgyId()));
            if (null != classify) {
                classify.setName(registerClassify.getName());
                if (registerClassifyService.update(classify) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
