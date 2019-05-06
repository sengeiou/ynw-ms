package com.ynw.system.controller;

import com.ynw.system.entity.WordFilter;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.WordFilterService;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/6
 */
@RestController
@RequestMapping("/ynw-ms/wordFilter")
@Api(tags = "敏感词接口",description = "敏感词查询及操作接口")
public class WordFilterController {

    @Autowired
    private WordFilterService wordFilterService;

    /**
     *  条件查询敏感词
     * @param wordFilter
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryWordFilter")
    @ApiOperation(value = "条件查询敏感词", notes = "传入：页码（nowPage必传），敏感词（word），类别（category）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryWordFilter(WordFilter wordFilter, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<WordFilter> wordFilterList = wordFilterService.conditionQueryWordFilter(wordFilter, start, pageSize);
            if (wordFilterList.size() > 0) {
                return ResultUtil.successResponse(wordFilterList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询敏感词数据总数
     * @param wordFilter
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询敏感词数据总数", notes = "传入：敏感词（word），类别（category）")
    public ResponseEntity<Result> count(WordFilter wordFilter) {
        Integer count = wordFilterService.count(wordFilter);
        return ResultUtil.successResponse(count);
    }

    /**
     *  添加敏感词
     * @param wordFilter
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加敏感词", notes = "传入：敏感词（word），类别（category），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(WordFilter wordFilter) {
        if (StringUtils.isNotEmpty(wordFilter.getWord()) && StringUtils.isNotEmpty(wordFilter.getCategory())) {
            if (wordFilterService.insert(wordFilter) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  修改敏感词
     * @param wordFilter
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "添加敏感词", notes = "传入：敏感词过滤id(syWordfilterId),敏感词（word），类别（category），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(WordFilter wordFilter) {
        if (StringUtils.isNotEmpty(wordFilter.getWord()) && StringUtils.isNotEmpty(wordFilter.getCategory())
                && StringUtils.isNotEmpty(wordFilter.getSyWordfilterId())) {
            WordFilter wordFilter1 = wordFilterService.selectOne(new WordFilter(wordFilter.getSyWordfilterId()));
            if (null != wordFilter1) {
                wordFilter1.setCategory(wordFilter.getCategory());
                wordFilter1.setWord(wordFilter.getWord());
                if (wordFilterService.update(wordFilter1) > 0) {
                    return ResultUtil.successResponse();
                }
                return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  删除敏感词
     * @param wordFilter
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除敏感词", notes = "传入：敏感词过滤id(syWordfilterId),添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(WordFilter wordFilter) {
        if (StringUtils.isNotEmpty(wordFilter.getSyWordfilterId())) {
            if (wordFilterService.delete(wordFilter) > 0) {
                return ResultUtil.successResponse();
            }
            return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
