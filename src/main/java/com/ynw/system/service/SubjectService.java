package com.ynw.system.service;

import com.ynw.system.dao.SubjectMapper;
import com.ynw.system.entity.Subject;
import com.ynw.system.entity.SubjectItem;
import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/15
 */
@Service
@Transactional
public class SubjectService extends MyService<SubjectMapper, Subject> {

    @Autowired
    private SubjectItemService subjectItemService;

    /**
     * 根据问卷查询题目
     * @param subject
     * @return
     */
    public List<Subject> conditionQuerySubject(Subject subject) {
        return dao.conditionQuerySubject(subject);
    }

    /**
     *  根据问卷获取降序数据
     * @return
     */
    public Subject findBySortMax(Subject subject) {
        return dao.findBySortMax(subject);
    }

    /**
     *  根据问卷和排序号查询题目
     * @param subject
     * @return
     */
    public List<Subject> findByExamIdAndSort(Subject subject) {
        return dao.findByExamIdAndSort(subject);
    }

    /**
     *  根据题目编号删除题目和题目选项
     * @param subject
     * @return
     */
    public Integer deleteSubject(Subject subject) {
        Integer code = 1;
        //删除相关题目选项
        SubjectItem subjectItem = new SubjectItem();
        subjectItem.setKbSubjectId(subject.getKbSubjectId());
        List<SubjectItem> itemList = subjectItemService.selectList(subjectItem);
        if (itemList.size() > 0) {
            code = subjectItemService.delete(subjectItem);
        }
        //删除题目
        if (code > 0) {
            code = dao.delete(subject);
        }
        //抛出个错误保证回滚
        if (code < 1) {
            throw new MyException(ResultEnums.ADDITION_FAILED);
        }
        return code;
     }

}
