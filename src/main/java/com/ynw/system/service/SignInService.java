package com.ynw.system.service;

import com.ynw.system.dao.SignInMapper;
import com.ynw.system.entity.SignIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  @author ChengZhi
 *  @version 2018-12/12
 */
@Service
@Transactional
public class SignInService {

    @Autowired
    private SignInMapper signInMapper;

    /**
     *  条件查询签到
     * @param signIn
     * @param start
     * @param pageSize
     * @return
     */
    public List<SignIn> conditionQuerySignIn(SignIn signIn, Integer start, Integer pageSize) {
        return signInMapper.conditionQuerySignIn(signIn, start, pageSize);
    }

    /**
     *  条件查询数据总和
     * @param signIn
     * @return
     */
    public Integer count(SignIn signIn) {
        return signInMapper.count(signIn);
    }

    /**
     *  查询当天签到数
     * @param nowTime
     * @return
     */
    public Integer nowCount(String nowTime) {
        return signInMapper.nowCount(nowTime);
    }

}
