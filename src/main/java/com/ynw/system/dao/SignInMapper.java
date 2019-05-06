package com.ynw.system.dao;

import com.ynw.system.entity.SignIn;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SignInMapper {

    List<SignIn> conditionQuerySignIn(@Param("signIn") SignIn signIn,
                                  @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    Integer count(SignIn signIn);

    Integer nowCount(String nowTime);

}
