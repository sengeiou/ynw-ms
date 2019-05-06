package com.ynw.system.controller;

import com.ynw.system.entity.InviteTheme;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.InviteThemeService;
import com.ynw.system.util.HandleFile;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ynw-ms/inviteInviteTheme")
@Api(tags = "用户约活动主题接口",description = "用户约活动主题查询及操作接口")
public class InviteThemeController {

    @Autowired
    private InviteThemeService inviteThemeService;
    @Autowired
    private HandleFile handleFile;

    /**
     *  根据编号查询数据
     * @param inviteThemeId
     * @return
     */
    @PostMapping("/findById")
    @ApiOperation(value = "根据编号查询约互动主题", notes = "传入：约活动主题id（inviteThemeId)(必传)")
    @ApiImplicitParam(name = "inviteThemeId", value = "约活动主题id", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findById(String inviteThemeId) {
        if (StringUtils.isEmpty(inviteThemeId))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        InviteTheme inviteTheme = inviteThemeService.selectOne(new InviteTheme(inviteThemeId));
        if (null == inviteTheme)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
        inviteTheme.setIcon(url + inviteTheme.getIcon());
        inviteTheme.setBgImageUrl(url + inviteTheme.getBgImageUrl());
        return ResultUtil.successResponse(inviteTheme);
    }

    /**
     *  根据编号更新数据
     * @param inviteTheme
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新用户标签", notes = "传入：约活动主题id（atInviteThemeId)，主题名称（name），icon图片文件（iconPhone），" +
            "背景图片文件（imageUrl），添加系统日志的主体（LogContent）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iconPhone",value = "icon图片文件",dataType = "file",paramType = "formData"),
            @ApiImplicitParam(name = "imageUrl", value = "背景图片文件",dataType = "file",paramType = "formData")
    })
    public ResponseEntity<Result> update(InviteTheme inviteTheme, HttpServletRequest request) throws IOException {
        if (StringUtils.isEmpty(inviteTheme.getAtInviteThemeId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        String icon = upload(request, "iconPhone");
        String bgImageUrl = upload(request, "imageUrl");
        if (null != icon) {
            inviteTheme.setIcon(icon);
        }
        if (null != bgImageUrl) {
            inviteTheme.setBgImageUrl(bgImageUrl);
        }
        inviteThemeService.updateById(inviteTheme);
        return ResultUtil.successResponse();
    }

    /**
     *  添加主题
     * @param inviteTheme
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加用户标签", notes = "传入：主题名称（name），icon图片文件（iconPhone），\" +\n" +
            "            \"背景图片文件（imageUrl），添加系统日志的主体（LogContent）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iconPhone",value = "icon图片文件",dataType = "file",paramType = "formData"),
            @ApiImplicitParam(name = "imageUrl", value = "背景图片文件",dataType = "file",paramType = "formData")
    })
    public ResponseEntity<Result> insert(InviteTheme inviteTheme, HttpServletRequest request) throws IOException {
        if (StringUtils.isEmpty(inviteTheme.getName()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        InviteTheme theme = inviteThemeService.findMaxSort();
        if (null != theme) {
            inviteTheme.setSort(theme.getSort() + 1);
        } else {
            inviteTheme.setSort(1);
        }
        String icon = upload(request, "iconPhone");
        String bgImageUrl = upload(request, "imageUrl");
        if (null != icon && null != bgImageUrl) {
            inviteTheme.setIcon(icon);
            inviteTheme.setBgImageUrl(bgImageUrl);
        } else {
            return ResultUtil.errorResponse(ResultEnums.FILE_ERROR);
        }
        if (inviteThemeService.insert(inviteTheme) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  条件分页查询数据
     * @param inviteTheme
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryInviteTheme")
    @ApiOperation(value = "条件分页查询数据", notes = "传入：页码（nowPage必传），主题名称（name）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryInviteTheme(InviteTheme inviteTheme, Integer nowPage) {
        if (null == nowPage || nowPage < 1)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<InviteTheme> inviteThemeList = inviteThemeService.conditionQueryInviteTheme(inviteTheme, start, pageSize);
        if (inviteThemeList.size() > 0) {
            String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
            for (InviteTheme theme: inviteThemeList
                 ) {
                theme.setBgImageUrl(url + theme.getBgImageUrl());
                theme.setIcon(url + theme.getIcon());
            }
            return ResultUtil.successResponse(inviteThemeList);
        }
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     * 条件分页查询数据
     * @param inviteTheme
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件分页查询数据", notes = "传入：主题名称（name）")
    public ResponseEntity<Result> count(InviteTheme inviteTheme) {
        return ResultUtil.successResponse(inviteThemeService.count(inviteTheme));
    }

    /**
     *  上传图片
     * @param request
     * @return
     */
    public String upload(HttpServletRequest request, String fileName) throws IOException {
        List<MultipartFile> file1 = new ArrayList<MultipartFile>();
        if (request instanceof MultipartHttpServletRequest) {
            file1 = ((MultipartHttpServletRequest) request).getFiles(fileName);
        }
        if (file1.get(0).getSize() > 0) {
            return handleFile.saveFile(file1.get(0) ,"push");
        }
        return null;
    }

}
