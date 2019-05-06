package com.ynw.system.controller;

import com.ynw.system.entity.AcUserLabel;
import com.ynw.system.entity.AcUserLabelClassify;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.AcUserLabelClassifyService;
import com.ynw.system.service.AcUserLabelService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  @author ChengZhi
 *  @version 2018-12/10
 */
@RestController
@RequestMapping("/ynw-ms/user_label")
@Api(tags = "用户标签接口",description = "用户标签查询及操作接口")
public class AcUserLabelController {

    @Autowired
    private AcUserLabelService labelService;
    @Autowired
    private AcUserLabelClassifyService labelClassifyService;

    /**
     *  条件查询用户标签
     * @param label
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryUserLabel")
    @ApiOperation(value = "根据条件分页查询用户标签记录", notes = "传入：页码（nowPage必传），标签名（name），用户标签分类id（acLabelCtgyId）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryUserLabel(AcUserLabel label, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<AcUserLabel> labels = labelService.conditionQueryAcUserLabel(label, pageSize, start);
            if (labels.size() > 0) {
                return ResultUtil.successResponse(labels);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询用户标签总数
     * @param label
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询用户标签总数", notes = "传入：页码（nowPage必传），标签名（name），用户标签分类id（acLabelCtgyId）")
    public ResponseEntity<Result> count(AcUserLabel label) {
        return ResultUtil.successResponse(labelService.count(label));
    }

    /**
     *  条件查询单条数据
     * @param label
     * @return
     */
    @PostMapping("/selectOne")
    @ApiOperation(value = "条件查询单条数据", notes = "传入：编号（acLabelId)，标签名（name），用户标签分类id（acLabelCtgyId）")
    public ResponseEntity<Result> selectOne(AcUserLabel label) {
//        if (StringUtils.isEmpty(label.getAcLabelId()))
//            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        AcUserLabel userLabel = labelService.selectOne(label);
        if (null != userLabel)
            return ResultUtil.successResponse(userLabel);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加用户标签
     * @param label
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加用户标签", notes = "传入：标签名（name），用户标签分类id（acLabelCtgyId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(AcUserLabel label) {
        if (StringUtils.isNotEmpty(label.getName()) && StringUtils.isNotEmpty(label.getAcLabelCtgyId())) {
            if (null != labelClassifyService.selectOne(new AcUserLabelClassify(label.getAcLabelCtgyId()))) {
                List<AcUserLabel> labels = labelService.findBySortMax(label.getAcLabelCtgyId());
                if (labels.size() > 0) {
                    if (null != labels.get(0).getSort()) {
                        label.setSort(labels.get(0).getSort() + 1);
                    }
                }
                if (labelService.insert(label) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新用户标签
     * @param label
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新用户标签", notes = "传入：编号（acLabelId)，标签名（name），用户标签分类id（acLabelCtgyId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(AcUserLabel label) {
        if (StringUtils.isNotEmpty(label.getName()) && StringUtils.isNotEmpty(label.getAcLabelId())
                && StringUtils.isNotEmpty(label.getAcLabelCtgyId())) {
            if (null != labelClassifyService.selectOne(new AcUserLabelClassify(label.getAcLabelCtgyId()))) {
                AcUserLabel userLabel = labelService.selectOne(new AcUserLabel(label.getAcLabelId()));
                if (null != userLabel) {
                    userLabel.setName(label.getName());
                    userLabel.setAcLabelCtgyId(label.getAcLabelCtgyId());
                    if (labelService.update(userLabel) > 0) {
                        return ResultUtil.successResponse();
                    }
                    return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
                }
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除用户标签
     * @param label
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除用户标签", notes = "传入：编号（acLabelId)，添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(AcUserLabel label) {
        if (StringUtils.isNotEmpty(label.getAcLabelId())) {
            if (labelService.delete(label) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param labelId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（labelId)(必传)")
    @ApiImplicitParam(name = "labelId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String labelId) {
        return move(labelId, 1);
    }

    /**
     *  下移
     * @param labelId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（labelId)(必传)")
    @ApiImplicitParam(name = "labelId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String labelId) {
        return move(labelId, -1);
    }

    /**
     *  上移下移复用
     * @param labelId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String labelId, Integer move) {
        if (StringUtils.isNotEmpty(labelId)) {
            AcUserLabel label = labelService.selectOne(new AcUserLabel(labelId));
            if (null == label)
                return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
            List<AcUserLabel> labelList = labelService.findBySortMax(label.getAcLabelCtgyId());
            if (null == labelList.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (labelList.size() > 1) {
                Integer code = labelService.move(labelId, labelList, move);
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
     *  查询用户所有标签
     * @return
     */
    @PostMapping("/findAllByUserId")
    @ApiOperation(value = "查询用户所有标签", notes = "传入：用户编号（userId)(必传)")
    @ApiImplicitParam(name = "userId", value = "用户编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findAllByUserId(String userId) {
        if (StringUtils.isEmpty(userId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        List<AcUserLabel> labelList = labelService.findAllByUserId(userId);
        if (labelList.size() == 0)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        Map<String, List<AcUserLabel>> map = new HashMap<>();
        for (AcUserLabel label: labelList
             ) {
            //如果标签分类相同，则存储入相同标签的list集合中
            if (map.containsKey(label.getAcLabelClassifyName())) {
                map.get(label.getAcLabelClassifyName()).add(label);
            } else {
                //否则新建一个list存储
                List<AcUserLabel> list = new ArrayList<>();
                list.add(label);
                map.put(label.getAcLabelClassifyName(), list);
            }
        }
        return ResultUtil.successResponse(map);
    }

}
