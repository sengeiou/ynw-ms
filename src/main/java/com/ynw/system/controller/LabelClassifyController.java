package com.ynw.system.controller;

import com.ynw.system.entity.AcUserLabelClassify;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.AcUserLabelClassifyService;
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
 *  @version 2019/2/18
 */
@RestController
@RequestMapping("/ynw-ms/user_label_classify")
@Api(tags = "用户标签类别接口",description = "用户标签类别查询及操作接口")
public class LabelClassifyController {

    @Autowired
    private AcUserLabelClassifyService labelClassifyService;


    /**
     *  查询所有用户标签类别
     * @return
     */
    @PostMapping("/findUserLabelClassify")
    @ApiOperation(value = "查询所有用户标签类别")
    public ResponseEntity<Result> findUserLabelClassify() {
        List<AcUserLabelClassify> labelClassifyList = labelClassifyService.findAll();
        if (labelClassifyList.size() > 0) {
            return ResultUtil.successResponse(labelClassifyList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  条件查询单个数据
     * @param labelClassify
     * @return
     */
    @PostMapping("/selectOne")
    @ApiOperation(value = "条件查询单个数据", notes = "传入：用户标签id(acLabelCtgyId),标签分类自定义key(key),标签内容(name),")
    public ResponseEntity<Result> selectOne(AcUserLabelClassify labelClassify) {
//        if (StringUtils.isEmpty(labelClassify.getAcLabelCtgyId()))
//            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        AcUserLabelClassify userLabelClassify = labelClassifyService.selectOne(labelClassify);
        if (null != userLabelClassify)
            return ResultUtil.successResponse(userLabelClassify);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加用户标签类别
     * @param labelClassify
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加用户标签类别", notes = "传入：标签分类自定义key(key),标签内容(name),添加系统日志的主体(LogContent)")
    public ResponseEntity<Result> insert(AcUserLabelClassify labelClassify) {
        if (StringUtils.isNotEmpty(labelClassify.getName()) && StringUtils.isNotEmpty(labelClassify.getKey())) {
            List<AcUserLabelClassify> labelClassifyList = labelClassifyService.findBySortMax();
            if (labelClassifyList.size() > 0) {
                if (null != labelClassifyList.get(0).getSort()) {
                    //数据库有数据并且最大的sort不为空
                    labelClassify.setSort(labelClassifyList.get(0).getSort() + 1);
                }
            }
            if (labelClassifyService.insert(labelClassify) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新用户标签类别
     * @param labelClassify
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新用户标签类别", notes = "传入：用户标签id(acLabelCtgyId),标签分类自定义key(key)," +
            "标签内容(name),添加系统日志的主体(LogContent)")
    public ResponseEntity<Result> update(AcUserLabelClassify labelClassify) {
        if (StringUtils.isNotEmpty(labelClassify.getName()) && StringUtils.isNotEmpty(labelClassify.getKey())
                && StringUtils.isNotEmpty(labelClassify.getAcLabelCtgyId())) {
            AcUserLabelClassify userLabelClassify = labelClassifyService.selectOne(new AcUserLabelClassify(labelClassify.getAcLabelCtgyId()));
            if (null != userLabelClassify) {
                userLabelClassify.setName(labelClassify.getName());
                userLabelClassify.setKey(labelClassify.getKey());
                if (labelClassifyService.update(userLabelClassify) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除用户标签类别
     * @param labelClassify
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除用户标签类别", notes = "传入：用户标签id(acLabelCtgyId),添加系统日志的主体(LogContent)")
    public ResponseEntity<Result> delete(AcUserLabelClassify labelClassify) {
        if (StringUtils.isNotEmpty(labelClassify.getAcLabelCtgyId())) {
            if (labelClassifyService.delete(labelClassify) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param labelClassifyId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（labelClassifyId)(必传)")
    @ApiImplicitParam(name = "labelClassifyId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String labelClassifyId) {
        return move(labelClassifyId, 1);
    }

    /**
     *  下移
     * @param labelClassifyId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（labelClassifyId)(必传)")
    @ApiImplicitParam(name = "labelClassifyId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String labelClassifyId) {
        return move(labelClassifyId, -1);
    }

    /**
     *  上移下移复用
     * @param labelClassifyId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String labelClassifyId, Integer move) {
        if (StringUtils.isNotEmpty(labelClassifyId)) {
            List<AcUserLabelClassify> labelClassifyList = labelClassifyService.findBySortMax();
            if (labelClassifyList.size() > 0) {
                if (null == labelClassifyList.get(0).getSort()) {
                    return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
                }
                Integer code = labelClassifyService.move(labelClassifyId, labelClassifyList, move);
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
