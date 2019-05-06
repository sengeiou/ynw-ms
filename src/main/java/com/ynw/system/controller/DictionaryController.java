package com.ynw.system.controller;

import com.ynw.system.entity.Dictionary;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.DictionaryService;
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
 *  @version 2018-12/6
 */
@RestController
@RequestMapping("/ynw-ms/dictionary")
@Api(tags = "字典标签接口",description = "字典查询及操作接口")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    /**
     *  根据组key查询字典
     * @param dictionary
     * @return
     */
    @PostMapping("/findDictionaryByGroupKey")
    @ApiOperation(value = "根据组key查询字典", notes = "传入：组键（groupKey）")
    public ResponseEntity<Result> findDictionaryByGroupKey(Dictionary dictionary) {
        if (StringUtils.isNotEmpty(dictionary.getGroupKey())) {
            List<Dictionary> dictionaryList = dictionaryService.findDictionaryByGroupKey(dictionary);
            if (dictionaryList.size() > 0) {
                return ResultUtil.successResponse(dictionaryList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  查询所有组键
     * @return
     */
    @PostMapping("/findDictionaryByGroupKy")
    @ApiOperation(value = "查询所有组键")
    public ResponseEntity<Result> findDictionaryByGroupKy() {
        List<Dictionary> dictionaries = dictionaryService.findDictionaryByGroupKy();
        if (dictionaries.size() > 0)
            return ResultUtil.successResponse(dictionaries);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据条件查询分页数据
     * @param dictionary
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryDictionary")
    @ApiOperation(value = "根据条件查询分页数据", notes = "传入：页码（nowPage必传），组键（groupKey）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryDictionary(Dictionary dictionary, Integer nowPage) {
        if (null == nowPage || nowPage == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<Dictionary> dictionaries = dictionaryService.conditionQueryDictionary(dictionary, pageSize, start);
        if (dictionaries.size() > 0)
            return ResultUtil.successResponse(dictionaries);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *  根据条件数据总数
     * @param dictionary
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "根据条件数据总数", notes = "传入：组键（groupKey）")
    public ResponseEntity<Result> count(Dictionary dictionary) {
        return ResultUtil.successResponse(dictionaryService.count(dictionary));
    }

    /**
     *  添加字典
     * @param dictionary
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加字典", notes = "传入：组键（groupKey），组描述（groupDesc），组描述（groupDesc），" +
            "键（itemKey），值（itemValue），描述（describe），排序（sort），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Dictionary dictionary) {
        if (StringUtils.isEmpty(dictionary.getGroupKey()) || StringUtils.isEmpty(dictionary.getDescribe())
                || StringUtils.isEmpty(dictionary.getItemKey()) || StringUtils.isEmpty(dictionary.getItemValue())
                || StringUtils.isEmpty(dictionary.getGroupDesc()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (dictionaryService.insert(dictionary) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  更新字典
     * @param dictionary
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新字典", notes = "传入：字典ID(syDictionaryId),组键（groupKey），组描述（groupDesc），组描述（groupDesc），" +
            "键（itemKey），值（itemValue），描述（describe），排序（sort），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Dictionary dictionary) {
        if (StringUtils.isEmpty(dictionary.getGroupKey()) || StringUtils.isEmpty(dictionary.getDescribe())
                || StringUtils.isEmpty(dictionary.getItemKey()) || StringUtils.isEmpty(dictionary.getItemValue())
                || StringUtils.isEmpty(dictionary.getGroupDesc()) || StringUtils.isEmpty(dictionary.getSyDictionaryId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Dictionary dictionary1 = dictionaryService.selectOne(new Dictionary(dictionary.getSyDictionaryId()));
        if (null == dictionary1)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        dictionary1.setGroupKey(dictionary.getGroupKey());
        dictionary1.setGroupDesc(dictionary.getGroupDesc());
        dictionary1.setItemKey(dictionary.getItemKey());
        dictionary1.setItemValue(dictionary.getItemValue());
        dictionary1.setDescribe(dictionary.getDescribe());
        dictionary1.setSort(dictionary.getSort());
        if (dictionaryService.update(dictionary1) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  删除字典
     * @param dictionary
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "更新字典", notes = "传入：字典ID(syDictionaryId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Dictionary dictionary) {
        if (StringUtils.isEmpty(dictionary.getSyDictionaryId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (dictionaryService.delete(dictionary) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

}
