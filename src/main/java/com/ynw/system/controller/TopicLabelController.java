package com.ynw.system.controller;

import com.ynw.system.entity.TopicLabel;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.TopicLabelService;
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

@RestController
@RequestMapping("/ynw-ms/topic")
@Api(tags = "话题标签接口",description = "话题标签查询及操作接口")
public class TopicLabelController {

    @Autowired
    private TopicLabelService topicLabelService;

    /**
     *  查询所有话题标签
     * @return
     */
    @PostMapping("/findAll")
    @ApiOperation(value = "查询所有话题标签")
    public ResponseEntity<Result> findAll() {
        List<TopicLabel> topicLabels = topicLabelService.findAll();
        if (topicLabels.size() > 0)
            return ResultUtil.successResponse(topicLabels);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  添加话题信息
     * @param topicLabel
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加话题信息", notes = "传入：标签名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(TopicLabel topicLabel) {
        if (StringUtils.isNotEmpty(topicLabel.getName())) {
            List<TopicLabel> topicLabels = topicLabelService.findBySortMax();
            if (topicLabels.size() > 0) {
                if (null != topicLabels.get(0).getSort()) {
                    //数据库有数据并且最大的sort不为空
                    topicLabel.setSort(topicLabels.get(0).getSort() + 1);
                }
            }
            if (topicLabelService.insert(topicLabel) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新话题标签
     * @param topicLabel
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新话题标签", notes = "传入：话题标签id(dsLabelId),标签名称（name），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(TopicLabel topicLabel) {
        if (StringUtils.isNotEmpty(topicLabel.getName()) && StringUtils.isNotEmpty(topicLabel.getDsLabelId())) {
            TopicLabel topicLabel1 = topicLabelService.findOne(new TopicLabel(topicLabel.getDsLabelId()));
            if (null != topicLabel1) {
                topicLabel1.setName(topicLabel.getName());
                if (topicLabelService.update(topicLabel1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除话题标签
     * @param topicLabel
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除话题标签", notes = "传入：话题标签id(dsLabelId),标签名称(name),是否被隐藏(isHidden),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(TopicLabel topicLabel) {
        if (StringUtils.isNotEmpty(topicLabel.getDsLabelId())) {
            if (topicLabelService.updateLabel(topicLabel) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param dsLabelId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（dsLabelId)(必传)")
    @ApiImplicitParam(name = "dsLabelId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String dsLabelId) {
        return move(dsLabelId, 1);
    }

    /**
     *  下移
     * @param dsLabelId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（dsLabelId)(必传)")
    @ApiImplicitParam(name = "dsLabelId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String dsLabelId) {
        return move(dsLabelId, -1);
    }

    /**
     *  上移下移复用
     * @param dsLabelId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String dsLabelId, Integer move) {
        if (StringUtils.isNotEmpty(dsLabelId)) {
            List<TopicLabel> topicLabels = topicLabelService.findBySortMax();
            if (null == topicLabels.get(0).getSort()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            if (topicLabels.size() > 1) {
                Integer code = topicLabelService.move(dsLabelId, topicLabels, move);
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
