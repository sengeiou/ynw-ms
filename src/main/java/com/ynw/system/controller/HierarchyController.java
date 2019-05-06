package com.ynw.system.controller;

import com.ynw.system.entity.Hierarchy;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.HierarchyService;
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
@RequestMapping("/ynw-ms/hierarchy")
@Api(tags = "用户等级接口",description = "用户等级查询及操作接口")
public class HierarchyController {

    @Autowired
    private HierarchyService hierarchyService;

    /**
     *  根据条件分页查询数据
     * @param hierarchy
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryHierarchy")
    @ApiOperation(value = "根据条件分页查询等级数据", notes = "传入：页码（nowPage必传），等级名称（name）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryHierarchy(Hierarchy hierarchy, Integer nowPage) {
        if (null == nowPage || nowPage == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<Hierarchy> hierarchyList = hierarchyService.conditionQueryHierarchy(hierarchy, pageSize, start);
        if (hierarchyList.size() > 0)
            return ResultUtil.successResponse(hierarchyList);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据条件查询数据总数
     * @param hierarchy
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "根据条件查询数据总数", notes = "传入：等级名称（name）")
    public ResponseEntity<Result> count(Hierarchy hierarchy) {
        return ResultUtil.successResponse(hierarchyService.count(hierarchy));
    }

    /**
     *  添加等级
     * @param hierarchy
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加等级", notes = "传入：等级名称（name），要求在等级有效期内累积获取到的最少情豆数量（needGetBean），" +
            "要求在等级有效期内累积支出的最少情豆数量（needPayoutBean）,添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Hierarchy hierarchy) {
        if (StringUtils.isNotEmpty(hierarchy.getName())) {
            List<Hierarchy> hierarchyList = hierarchyService.findByNoMax();
            if (hierarchyList.size() > 0) {
                hierarchy.setNo(hierarchyList.get(0).getNo() + 1);
            }
            if (hierarchyService.insert(hierarchy) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  更新等级
     * @param hierarchy
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新等级", notes = "传入：用户等级id(acULevelId),等级名称（name），要求在等级有效期内累积获取到的最少情豆数量（needGetBean），" +
            "要求在等级有效期内累积支出的最少情豆数量（needPayoutBean）,添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Hierarchy hierarchy) {
        if (StringUtils.isNotEmpty(hierarchy.getName()) && StringUtils.isNotEmpty(hierarchy.getAcULevelId())) {
            Hierarchy hierarchy1 = hierarchyService.selectOne(new Hierarchy(hierarchy.getAcULevelId()));
            if (null != hierarchy1) {
                hierarchy1.setName(hierarchy.getName());
                hierarchy1.setNeedGetBean(hierarchy.getNeedGetBean());
                hierarchy1.setNeedPayoutBean(hierarchy.getNeedPayoutBean());
                if (hierarchyService.update(hierarchy1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除等级
     * @param hierarchy
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "更新等级", notes = "传入：用户等级id(acULevelId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Hierarchy hierarchy) {
        if (StringUtils.isNotEmpty(hierarchy.getAcULevelId())) {
            if (hierarchyService.delete(hierarchy) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  上移
     * @param acULevelId 移动对象编号
     * @return
     */
    @PostMapping("/moveUp")
    @ApiOperation(value = "上移", notes = "传入：移动对象编号（acULevelId)(必传)")
    @ApiImplicitParam(name = "acULevelId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveUp(String acULevelId) {
        return move(acULevelId, 1);
    }

    /**
     *  下移
     * @param acULevelId 移动对象编号
     * @return
     */
    @PostMapping("/moveDown")
    @ApiOperation(value = "下移", notes = "传入：移动对象编号（acULevelId)(必传)")
    @ApiImplicitParam(name = "acULevelId", value = "移动对象编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> moveDown(String acULevelId) {
        return move(acULevelId, -1);
    }

    /**
     *  上移下移复用
     * @param acULevelId 移动对象编号
     * @return
     */
    public ResponseEntity<Result> move(String acULevelId, Integer move) {
        if (StringUtils.isNotEmpty(acULevelId)) {
            List<Hierarchy> hierarchyList = hierarchyService.findByNoMax();
            if (null == hierarchyList.get(0).getNo()) {
                return ResultUtil.errorResponse(ResultEnums.UNSORTED_ERROR);
            }
            Integer code = hierarchyService.move(acULevelId, hierarchyList, move);
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
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
