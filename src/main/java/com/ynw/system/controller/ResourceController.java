package com.ynw.system.controller;

import com.ynw.system.entity.Resource;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ResourceService;
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
@RequestMapping("/ynw-ms/resource")
@Api(tags = "管理员资源接口",description = "管理员资源查询及操作接口")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     *  添加资源
     * @param resource 传入（必传）描述（description），资源名（name），来源URL（sourceUrl），
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加资源", notes = "传入：描述（describe），是否隐藏（isHide），添加系统日志的主体（LogContent）" +
            "，层级（level），资源名（name），排序（sort），来源Key（sourceKey），来源URL（sourceUrl），资源类型（type），" +
            "父资源ID（parentId）")
    public ResponseEntity<Result> inset(Resource resource) {
        if (StringUtils.isNotEmpty(resource.getDescribe()) && StringUtils.isNotEmpty(resource.getName())
                && StringUtils.isNotEmpty(resource.getSourceUrl())) {
            if (StringUtils.isEmpty(resource.getParentId())) {
                resource.setParentId(null);
            }
            if (resourceService.insert(resource) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改资源
     * @param resource 传入（必传）描述（description），资源名（name），来源URL（sourceUrl），
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改资源", notes = "传入：资源ID（msResourceId），描述（describe），是否隐藏（isHide），添加系统日志的主体（LogContent）" +
            "，层级（level），资源名（name），排序（sort），来源Key（sourceKey），来源URL（sourceUrl），资源类型（type），" +
            "父资源ID（parentId）")
    public ResponseEntity<Result> update(Resource resource) {
        if (StringUtils.isNotEmpty(resource.getDescribe()) && StringUtils.isNotEmpty(resource.getName())
                && StringUtils.isNotEmpty(resource.getSourceUrl()) && StringUtils.isNotEmpty(resource.getMsResourceId())) {
            Resource resource1 = resourceService.selectOne(new Resource(resource.getMsResourceId()));
            if (null != resource1) {
                //赋值
                resource1.setIsHide(resource.getIsHide());
                resource1.setDescribe(resource.getDescribe());
                resource1.setLevel(resource.getLevel());
                resource1.setName(resource.getName());
                resource1.setParentId(resource.getParentId());
                resource1.setSort(resource.getSort());
                resource1.setSourceKey(resource.getSourceKey());
                resource1.setSourceUrl(resource.getSourceUrl());
                resource1.setType(resource.getType());
                if (resourceService.update(resource1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除资源
     * @param resource 传入（必传）描述（description）
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除资源", notes = "传入：资源ID（msResourceId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Resource resource) {
        if (StringUtils.isNotEmpty(resource.getMsResourceId())) {
            if (resourceService.delete(resource) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询管理员的资源
     * @param msUserId 管理员账号
     * @param resource
     * @return
     */
    @PostMapping("/conditionQueryResourceByUserId")
    @ApiOperation(value = "条件查询管理员的资源", notes = "传入：管理员账号（msUserId)(必传），层级（level），是否隐藏（isHide）" +
            "，资源类型（type），父资源ID（parentId）")
    @ApiImplicitParam(name = "msUserId", value = "管理员账号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> conditionQueryResourceByUserId(String msUserId, Resource resource) {
        if (StringUtils.isNotEmpty(msUserId)) {
            List<Resource> resourceList = resourceService.conditionQueryResourceByUserId(msUserId, resource);
            if (resourceList.size() > 0) {
                return ResultUtil.successResponse(resourceList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  查询角色下的资源
     * @param roleId 角色编号
     * @return
     */
    @PostMapping("/findResourceByRoleId")
    @ApiOperation(value = "查询角色下的资源", notes = "传入：角色编号（roleId)(必传）")
    @ApiImplicitParam(name = "roleId", value = "角色编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findResourceByRoleId(String roleId) {
        if (StringUtils.isNotEmpty(roleId)) {
            List<Resource> resourceList = resourceService.findResourceByRoleId(roleId);
            if (resourceList.size() > 0) {
                return ResultUtil.successResponse(resourceList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询资源
     * @param resource
     * @return
     */
    @PostMapping("/conditionQueryResourceAll")
    @ApiOperation(value = "条件查询资源", notes = "传入：页码（nowPage）(必传），资源名（name），资源类型（type）" +
            "，层级（level），是否隐藏（isHide）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryUserAll(Resource resource, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<Resource> resourceList = resourceService.conditionQueryResourceAll(resource, start, pageSize);
            if (resourceList.size() > 0) {
                return ResultUtil.successResponse(resourceList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  查询所有
     * @return
     */
    @PostMapping("/findAll")
    @ApiOperation(value = "查询所有")
    public ResponseEntity<Result> findAll() {
        Resource resource = new Resource();
        resource.setIsHide(1);
        List<Resource> resourceList = resourceService.selectList(resource);
        if (resourceList.size() > 0) {
            return ResultUtil.successResponse(resourceList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  条件查询单个
     * @return
     */
    @PostMapping("/selectOne")
    @ApiOperation(value = "条件查询单个", notes = "传入：资源ID（msResourceId），描述（describe），是否隐藏（isHide），" +
            "，层级（level），资源名（name），排序（sort），来源Key（sourceKey），来源URL（sourceUrl），资源类型（type），" +
            "父资源ID（parentId）")
    public ResponseEntity<Result> selectOne(Resource resource) {
        Resource resource1 = resourceService.selectOne(resource);
        if (null != resource1) {
            return ResultUtil.successResponse(resource1);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  条件统计数据
     * @param resource
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件统计数据", notes = "传入：资源名（name），资源类型（type）" +
            "，层级（level），是否隐藏（isHide）")
    public ResponseEntity<Result> count(Resource resource) {
        Integer count = resourceService.count(resource);
        return ResultUtil.successResponse(count);
    }

    /**
     *  查询目录下的所有子集
     * @return
     */
    @PostMapping("/relevanceFindResource")
    @ApiOperation(value = "查询目录下的所有子集")
    public ResponseEntity<Result> relevanceFindResource() {
        List<Resource> resourceList = resourceService.relevanceFindResource();
        if (resourceList.size() > 0) {
            return ResultUtil.successResponse(resourceList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  获取type为菜单和目录的资源
     * @return
     */
    @PostMapping("/findByTypeLessThanOne")
    @ApiOperation(value = "获取type为菜单和目录的资源")
    public ResponseEntity<Result> findByTypeLessThanOne() {
        List<Resource> resourceList = resourceService.findByTypeLessThanOne();
        if (resourceList.size() > 0) {
            return ResultUtil.successResponse(resourceList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

}
