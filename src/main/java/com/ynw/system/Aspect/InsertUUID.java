package com.ynw.system.Aspect;

import com.ynw.system.controller.UserController;
import com.ynw.system.entity.Entity;
import com.ynw.system.entity.OptLog;
import com.ynw.system.service.OptLogService;
import com.ynw.system.util.JsonUtils;
import com.ynw.system.util.Result;
import com.ynw.system.util.UUIDUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Aspect
@Component
public class InsertUUID {

    @Autowired
    private OptLogService optLogService;

    @Pointcut(value = "execution(int com.ynw.system.dao.*.insert(..))")
    public void insertPoint(){}
    @Pointcut("execution(* com.ynw.system.dao.*.definedInsert(..))")
    public  void point(){}
    @Pointcut("execution(* com.ynw.system.dao.*.insertMember(..))")
    public  void insertMember(){}


    /**
     *  获取编号
     * @param joinPoint
     */
    @Before(value = "insertPoint() || point() || insertMember()")
    public void BeforeInsert(JoinPoint joinPoint){
            //System.out.println( UUIDUtil.getUUID());
            Object[] objects=joinPoint.getArgs();
            //Class classType=objects[0].getClass();
            Entity entity= (Entity) objects[0];
            try {
                entity.setId(UUIDUtil.getEUUID());
            } catch (Exception e) {
                e.printStackTrace();
            }
            objects[0]=entity;


        }

}
