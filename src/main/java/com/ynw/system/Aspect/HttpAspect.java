package com.ynw.system.Aspect;

import com.ynw.system.util.JsonUtils;
import com.ynw.system.util.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * aop日志
 */
@Aspect
@Component
public class HttpAspect {

    private final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution(public * com.ynw.system.controller.*.*(..))")
    public void log(){
    }


    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Cookie[] cookies=request.getCookies();
        if (null!=cookies&&cookies.length>0){
        for (Cookie cookie:cookies){
            logger.info("接受到的cookie"+cookie.getName()+cookie.getValue());
        }}else {
            logger.info("没有cookie");
        }
        //获取url
        logger.info("url={}",request.getRequestURL());
        //获取method
        logger.info("method={}",request.getMethod());
        //获取ip
        logger.info("ip={}",request.getRemoteAddr());
        //获取类方法
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        //获取参数
        logger.info("args={}",joinPoint.getArgs());

    }

    @After("log()")
    public  void  doAfter(){

    }

    @AfterReturning(returning = "object",pointcut = "log()")
    public void doAfterReturn(Object object){
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        if (!request.getRequestURL().toString().contains("/ynw-ms/register/exportExcel")) {
            if (!request.getRequestURL().toString().contains("/ynw-ms/acUser/activeYesterday")) {
                if (!request.getRequestURL().toString().contains("/ynw-ms/acUser/yesterdayRegistered")) {
                    if (!request.getRequestURL().toString().contains("/ynw-ms/acUser/yesterdayStatistics")) {
                        //返回结果打印
//                        logger.info("response={}",object.toString());
                        ResponseEntity<Result> resultResponseEntity= (ResponseEntity<Result>) object;
                        Result result=resultResponseEntity.getBody();
                        if (result.getResult() instanceof Integer) {
                        } else {
                            if(result.getResult() instanceof List){
                                result.setResult(JsonUtils.stringToList(JsonUtils.objectToString(result.getResult()),Object.class));
                            }else {
                                result.setResult(JsonUtils.stringToObject(JsonUtils.objectToString(result.getResult()), Object.class));
                            }
                        }
                    }
                }
            }
        }
    }
}
