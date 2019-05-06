package com.ynw.system.controller;

import com.ynw.system.entity.SignIn;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.SignInService;
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

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@RestController
@RequestMapping("/ynw-ms/signIn")
@Api(tags = "用户签到接口",description = "用户签到查询接口")
public class SignInController {

    @Autowired
    private SignInService signInService;

    /**
     *  条件查询签到
     * @param signIn
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQuerySignIn")
    @ApiOperation(value = "条件查询签到", notes = "传入：页码（nowPage必传），用户电话（acUserPhone），签到日（signinDate）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQuerySignIn(SignIn signIn, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<SignIn> signInList = signInService.conditionQuerySignIn(signIn, start, pageSize);
            if (signInList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                for (SignIn signIn1: signInList
                     ) {
                    signIn1.setAcUserImg(url + signIn1.getAcUserImg());
                }
                return ResultUtil.successResponse(signInList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总和
     * @param signIn
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总和", notes = "传入：用户电话（acUserPhone），签到日（signinDate）")
    public ResponseEntity<Result> count(SignIn signIn) {
        return ResultUtil.successResponse(signInService.count(signIn));
    }

}
