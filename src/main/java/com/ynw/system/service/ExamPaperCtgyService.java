package com.ynw.system.service;

import com.ynw.system.dao.ExamPaperCtgyMapper;
import com.ynw.system.dao.ReportTypeMapper;
import com.ynw.system.entity.ExamPaperCtgy;
import com.ynw.system.entity.ReportType;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/13
 */
@Service
@Transactional
public class ExamPaperCtgyService extends MyService<ExamPaperCtgyMapper, ExamPaperCtgy> {

    /**
     *  查询所有
     * @return
     */
    public List<ExamPaperCtgy> findAll() {
        return dao.findAll();
    }

    /**
     *  获取降序数据
     * @return
     */
    public List<ExamPaperCtgy> findBySortMax() {
        return dao.findBySortMax();
    }

    /**
     *  上移下移复用
     * @param kbExampaperCtgyId 移动对象编号
     * @param examPaperCtgyList 全体数据（降序）
     * @param move 判断是上移还是下移（-1为下移，1为上移）
     * @return
     */
    public Integer move(String kbExampaperCtgyId, List<ExamPaperCtgy> examPaperCtgyList, Integer move) {
        Integer code = 0;
        Integer size = examPaperCtgyList.size();
        for (int i = 0; i < size; i++
        ) {
            if (examPaperCtgyList.get(i).getKbExampaperCtgyId().equals(kbExampaperCtgyId)) {
                if (i == 0 && move == -1) {
                    code = 15;
                } else if (i == size -1 && move == 1) {
                    code = 14;
                } else {
                    Integer sort = examPaperCtgyList.get(i + move).getSort();
                    examPaperCtgyList.get(i + move).setSort(examPaperCtgyList.get(i).getSort());
                    code = this.update(examPaperCtgyList.get(i + move));
                    if (code > 0) {
                        examPaperCtgyList.get(i).setSort(sort);
                        code = this.update(examPaperCtgyList.get(i));
                    }
                }
                break;
            }
        }
        if (code < 1) {
            //抛出一个错误保证回滚
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
    }

}
