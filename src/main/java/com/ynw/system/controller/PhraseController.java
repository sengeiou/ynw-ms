package com.ynw.system.controller;

import com.ynw.system.entity.Phrase;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.PhraseService;
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
@RequestMapping("/ynw-ms/phrase")
@Api(tags = "雷达短语接口",description = "雷达短语查询及操作接口")
public class PhraseController {

    @Autowired
    private PhraseService phraseService;

    /**
     *  根据条件分页查询数据
     * @param phrase
     * @param nowPage
     * @return
     */
    @PostMapping("/conditionQueryPhrase")
    @ApiOperation(value = "根据条件分页查询数据", notes = "传入：页码（nowPage必传）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryPhrase(Phrase phrase, Integer nowPage) {
        if (null == nowPage || nowPage == 0)
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Integer pageSize = 10;
        Integer start = (nowPage - 1) * pageSize;
        List<Phrase> phraseList = phraseService.conditionQueryPhrase(phrase, pageSize, start);
        if (phraseList.size() > 0)
            return ResultUtil.successResponse(phraseList);
        return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
    }

    /**
     *   条件查询数据总数
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总数")
    public ResponseEntity<Result> count(Phrase phrase) {
        return ResultUtil.successResponse(phraseService.selectCount(phrase));
    }

    /**
     *  添加雷达短语
     * @param phrase
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加雷达短语", notes = "传入：短语内容（content），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> insert(Phrase phrase) {
        if (StringUtils.isEmpty(phrase.getContent()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (phraseService.insert(phrase) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  修改雷达短语
     * @param phrase
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改雷达短语", notes = "传入：短语编号（rsPhraseId），短语内容（content），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> update(Phrase phrase) {
        if (StringUtils.isEmpty(phrase.getContent()) || StringUtils.isEmpty(phrase.getRsPhraseId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        Phrase phrase1 = phraseService.selectOne(new Phrase(phrase.getRsPhraseId()));
        if (null == phrase1)
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        phrase1.setContent(phrase.getContent());
        if (phraseService.update(phrase1) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

    /**
     *  删除雷达短语
     * @param phrase
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除雷达短语", notes = "传入：短语编号（rsPhraseId），添加系统日志的主体（LogContent）")
    public ResponseEntity<Result> delete(Phrase phrase) {
        if (StringUtils.isEmpty(phrase.getRsPhraseId()))
            return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
        if (phraseService.delete(phrase) > 0)
            return ResultUtil.successResponse();
        return ResultUtil.errorResponse(ResultEnums.ADDITION_FAILED);
    }

}
