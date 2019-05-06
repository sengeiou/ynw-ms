package com.ynw.system.controller;

import com.ynw.system.entity.Interest;
import com.ynw.system.entity.SelfLabel;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.InterestService;
import com.ynw.system.service.SelfLabelService;
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
@RequestMapping("/ynw-ms/selfLabel")
@Api(tags = "自我标签接口",description = "自我标签查询及操作接口")
public class SelfLabelController {

    @Autowired
    private SelfLabelService selfLabelService;

    /**
     *  条件查询自我标签
     * @param selfLabel
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQuerySelfLabel")
    @ApiOperation(value = "条件查询自我标签", notes = "传入：页码（nowPage必传），自我标签名称（name），")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQuerySelfLabel(SelfLabel selfLabel, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<SelfLabel> selfLabelList = selfLabelService.conditionQuerySelfLabel(selfLabel, start, pageSize);
            if (selfLabelList.size() > 0) {
                return ResultUtil.successResponse(selfLabelList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询自我标签总数
     * @param selfLabel
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询自我标签", notes = "传入：自我标签名称（name），")
    public ResponseEntity<Result> count(SelfLabel selfLabel) {
        return ResultUtil.successResponse(selfLabelService.count(selfLabel));
    }

    /**
     *  添加自我标签
     * @param selfLabel
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加自我标签", notes = "传入：自我标签名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(SelfLabel selfLabel) {
        if (StringUtils.isNotEmpty(selfLabel.getName())) {
            List<SelfLabel> selfLabelList = selfLabelService.findBySortMax();
            if (selfLabelList.size() > 0) {
                selfLabel.setSort(selfLabelList.get(0).getSort() + 1);
            } else {
                selfLabel.setSort(1);
            }
            if (selfLabelService.insert(selfLabel) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新自我标签
     * @param selfLabel
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新自我标签", notes = "传入：自我标签id（bdSelflabelId），自我标签名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(SelfLabel selfLabel) {
        if (StringUtils.isNotEmpty(selfLabel.getName()) && StringUtils.isNotEmpty(selfLabel.getBdSelflabelId())) {
            SelfLabel selfLabel1 = selfLabelService.selectOne(new SelfLabel(selfLabel.getBdSelflabelId()));
            if (null != selfLabel1) {
                selfLabel1.setName(selfLabel.getName());
                if (selfLabelService.update(selfLabel1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除自我标签
     * @param selfLabel
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除自我标签", notes = "传入：自我标签id（bdSelflabelId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(SelfLabel selfLabel) {
        if (StringUtils.isNotEmpty(selfLabel.getBdSelflabelId())) {
            if (selfLabelService.delete(selfLabel) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param bdSelflabelId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（bdSelflabelId)(必传)")
    @ApiImplicitParam(name = "bdSelflabelId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String bdSelflabelId) {
        return move(bdSelflabelId, 1);
    }

    /**
     *  下移
     * @param bdSelflabelId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（bdSelflabelId)(必传)")
    @ApiImplicitParam(name = "bdSelflabelId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String bdSelflabelId) {
        return move(bdSelflabelId, -1);
    }

    /**
     *  上移下移复用
     * @param bdSelflabelId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String bdSelflabelId, Integer move) {
        if (StringUtils.isNotEmpty(bdSelflabelId)) {
            List<SelfLabel> selfLabelList = selfLabelService.findBySortMax();
            if (null == selfLabelList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (selfLabelList.size() > 1) {
                Integer code = selfLabelService.move(bdSelflabelId, selfLabelList, move);
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
     *  根据用户编号查询自我标签
     * @param acUserId 用户编号
     * @return
     */
    @PostMapping("/findSelfLabelByAcUserId")
    @ApiOperation(value = "根据用户编号查询自我标签", notes = "传入：用户编号（acUserId)(必传)")
    @ApiImplicitParam(name = "acUserId", value = "用户编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findSelfLabelByAcUserId(String acUserId) {
        if (StringUtils.isNotEmpty(acUserId)) {
            List<SelfLabel> interestList = selfLabelService.findSelfLabelByAcUserId(acUserId);
            if (interestList.size() > 0) {
                return ResultUtil.successResponse(interestList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
