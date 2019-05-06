package com.ynw.system.controller;

import com.ynw.system.entity.Business;
import com.ynw.system.entity.Interest;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.BusinessService;
import com.ynw.system.service.InterestService;
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
 *  @version 2018-12/10
 */
@RestController
@RequestMapping("/ynw-ms/interest")
@Api(tags = "兴趣标签接口",description = "兴趣标签查询及操作接口")
public class InterestController {

    @Autowired
    private InterestService interestService;

    /**
     *  条件查询兴趣标签
     * @param interest
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryInterest")
    @ApiOperation(value = "条件查询兴趣标签", notes = "传入：页码（nowPage必传），标签名（name）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryInterest(Interest interest, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Interest> interestList = interestService.conditionQueryInterest(interest, start, pageSize);
            if (interestList.size() > 0) {
                return ResultUtil.successResponse(interestList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询兴趣标签总数
     * @param interest
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询兴趣标签", notes = "传入：标签名（name）")
    public ResponseEntity<Result> count(Interest interest) {
        return ResultUtil.successResponse(interestService.count(interest));
    }

    /**
     *  添加兴趣标签
     * @param interest
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加兴趣标签", notes = "传入：兴趣标签名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Interest interest) {
        if (StringUtils.isNotEmpty(interest.getName())) {
            List<Interest> interestList = interestService.findBySortMax();
            if (interestList.size() > 0) {
                if (null != interestList.get(0).getSort()) {
                    interest.setSort(interestList.get(0).getSort() + 1);
                }
            }
            if (interestService.insert(interest) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新兴趣标签
     * @param interest
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新兴趣标签", notes = "传入：兴趣标签id(bdInterestId),兴趣标签名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Interest interest) {
        if (StringUtils.isNotEmpty(interest.getName()) && StringUtils.isNotEmpty(interest.getBdInterestId())) {
            Interest interest1 = interestService.selectOne(new Interest(interest.getBdInterestId()));
            if (null != interest1) {
                interest1.setName(interest.getName());
                if (interestService.update(interest1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除兴趣标签
     * @param interest
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除兴趣标签", notes = "传入：兴趣标签id(bdInterestId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Interest interest) {
        if (StringUtils.isNotEmpty(interest.getBdInterestId())) {
            if (interestService.delete(interest) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param bdInterestId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（bdInterestId)(必传)")
    @ApiImplicitParam(name = "bdInterestId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String bdInterestId) {
        return move(bdInterestId, 1);
    }

    /**
     *  下移
     * @param bdInterestId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（bdInterestId)(必传)")
    @ApiImplicitParam(name = "bdInterestId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String bdInterestId) {
        return move(bdInterestId, -1);
    }

    /**
     *  上移下移复用
     * @param bdInterestId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String bdInterestId, Integer move) {
        if (StringUtils.isNotEmpty(bdInterestId)) {
            List<Interest> interestList = interestService.findBySortMax();
            if (null == interestList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (interestList.size() > 1) {
                Integer code = interestService.move(bdInterestId, interestList, move);
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

    /**
     *  根据用户编号查询兴趣标签
     * @param acUserId 用户编号
     * @return
     */
    @PostMapping("/findInterestByAcUserId")
    @ApiOperation(value = "根据用户编号查询兴趣标签", notes = "传入：用户编号（acUserId)(必传)")
    @ApiImplicitParam(name = "acUserId", value = "用户编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findInterestByAcUserId(String acUserId) {
        if (StringUtils.isNotEmpty(acUserId)) {
            List<Interest> interestList = interestService.findInterestByAcUserId(acUserId);
            if (interestList.size() > 0) {
                return ResultUtil.successResponse(interestList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
