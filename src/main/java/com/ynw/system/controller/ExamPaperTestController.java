package com.ynw.system.controller;

import com.ynw.system.entity.ExamPaperAnswerFlow;
import com.ynw.system.entity.ExamPaperTest;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.service.ExamPaperAnswerFlowService;
import com.ynw.system.service.ExamPaperTestService;
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
 *  @version 2018-12/14
 */
@RestController
@RequestMapping("/ynw-ms/examPaperTest")
@Api(tags = "问卷测试接口",description = "问卷测试查询接口")
public class ExamPaperTestController {

    @Autowired
    private ExamPaperTestService examPaperTestService;
    @Autowired
    private ExamPaperAnswerFlowService examPaperAnswerFlowService;

    /**
     *  条件查询问卷测试
     * @param examPaperTest
     * @param nowPage 当前页
     * @return
     */
    @PostMapping("/conditionQueryExamPaperTest")
    @ApiOperation(value = "条件查询问卷测试", notes = "传入：页码（nowPage必传），用户手机（userPhone），问卷标题（模糊查询）（examTitle）")
    @ApiImplicitParam(name = "nowPage", value = "页码", required = true, dataType = "Long", paramType = "query",example = "1")
    public ResponseEntity<Result> conditionQueryExamPaperTest(ExamPaperTest examPaperTest, Integer nowPage) {
        if (null != nowPage && nowPage > 0) {
            Integer pageSize = 10;
            Integer start = (nowPage - 1) * pageSize;
            List<ExamPaperTest> examPaperTestList = examPaperTestService.conditionQueryExamPaperTest(examPaperTest, start, pageSize);
            if (examPaperTestList.size() > 0) {
                String url = ParaConfController.paraConfController.findUrlByKey("URL_ROOT_PATH");
                if (StringUtils.isNotEmpty(url)) {
                    for (ExamPaperTest test :examPaperTestList
                    ) {
                        test.setUserImage(url+test.getUserImage());
                    }
                }
                return ResultUtil.successResponse(examPaperTestList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  条件查询数据总数
     * @param examPaperTest
     * @return
     */
    @PostMapping("/count")
    @ApiOperation(value = "条件查询数据总数", notes = "传入：用户手机（userPhone），问卷标题（模糊查询）（examTitle）")
    public ResponseEntity<Result> count(ExamPaperTest examPaperTest) {
        return ResultUtil.successResponse(examPaperTestService.count(examPaperTest));
    }

    /**
     *  根据用户编号查询测试
     * @param acUserId
     * @return
     */
    @PostMapping("/findByUserId")
    @ApiOperation(value = "根据用户编号查询测试", notes = "传入：用户编号（acUserId)(必传)")
    @ApiImplicitParam(name = "acUserId", value = "用户编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findByUserId(String acUserId) {
        if (StringUtils.isNotEmpty(acUserId)) {
            List<ExamPaperTest> examPaperTestList = examPaperTestService.findByUserId(acUserId);
            if (examPaperTestList.size() > 0) {
                return ResultUtil.successResponse(examPaperTestList);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

    /**
     *  根据用户考试编号查询流水记录
     * @param kbExampaperTestId 考试编号
     * @return
     */
    @PostMapping("/findByExamId")
    @ApiOperation(value = "根据用户编号查询测试", notes = "传入：考试编号（kbExampaperTestId)(必传)")
    @ApiImplicitParam(name = "kbExampaperTestId", value = "考试编号", required = true, dataType = "String", paramType = "query")
    public ResponseEntity<Result> findByExamId(String kbExampaperTestId) {
        if (StringUtils.isNotEmpty(kbExampaperTestId)) {
            List<ExamPaperAnswerFlow> examPaperAnswerFlows = examPaperAnswerFlowService.findByExamId(kbExampaperTestId);
            if (examPaperAnswerFlows.size() > 0) {
                return ResultUtil.successResponse(examPaperAnswerFlows);
            }
            return ResultUtil.errorResponse(ResultEnums.NO_DATA_ERROR);
        }
        return ResultUtil.errorResponse(ResultEnums.INCOMPLETE_INFORMATION);
    }

}
