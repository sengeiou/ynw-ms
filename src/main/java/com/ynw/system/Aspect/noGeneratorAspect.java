package com.ynw.system.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//@Aspect
//@Component
public class noGeneratorAspect {
//    @Resource
//    private UserService userService;
//    @Pointcut("execution(int com.ynw.dao.AcUserMapper.insert(..))")
//    public void UserPointcut(){}
//    @Before("UserPointcut()")
//  public void BeforeInsert(JoinPoint joinPoint){
//        Object[] objects=joinPoint.getArgs();
//        AcUser acUsers= (AcUser) objects[0];
//        acUsers.setNo(getNo());
//        objects[0]=acUsers;
//    }
//    public String getNo(){
//       Integer v= userService.getMaxNo();
//        return String.valueOf(v+1);
//    }
}
