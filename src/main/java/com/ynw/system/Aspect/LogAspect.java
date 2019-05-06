package com.ynw.system.Aspect;

import com.ynw.system.controller.UserController;
import com.ynw.system.entity.OptLog;
import com.ynw.system.service.OptLogService;
import com.ynw.system.util.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * aop日志
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private OptLogService logService;

    @Pointcut("execution(public * com.ynw.system.controller.*.insert(..))")
    public void insert(){}
    @Pointcut("execution(public * com.ynw.system.controller.*.update(..))")
    public void update(){}
    @Pointcut("execution(public * com.ynw.system.controller.*.delete(..))")
    public void delete(){}
    @Pointcut("execution(public * com.ynw.system.controller.*.updateStatus(..))")
    public void updateStatus(){}
    @Pointcut("execution(public * com.ynw.system.controller.*.updatePassword(..))")
    public void updatePassword(){}
    @Pointcut("execution(public * com.ynw.system.controller.*.replaceHeadById(..))")
    public void replaceHeadById(){}

    private Map<String, OptLog> logMap = new HashMap<>();
//    private LogAspect logAspect = new LogAspect();

    @Before("insert()")
    public void doBefore(JoinPoint joinPoint){
        reuse(joinPoint,"新增");
    }

    @AfterReturning(returning = "object",pointcut = "insert()||update()||delete()||updatePassword()||updateStatus()")
    public void doAfterReturn(Object object){
        ResponseEntity<Result> resultResponseEntity= (ResponseEntity<Result>) object;
        Result result=resultResponseEntity.getBody();
        if (result.getCode() != 1) {
            OptLog log = logMap.get("log");
//            System.out.println(log.toString()+"-----------------");
            log.setContent(log.getContent()+"【操作失败】");
            logService.insert(log);
        }
    }

    @Before("update()||updatePassword()||updateStatus()||replaceHeadById()")
    public void updateBefore(JoinPoint joinPoint){
        reuse(joinPoint,"更新");
    }

    @Before("delete()")
    public void deleteBefore(JoinPoint joinPoint){
        reuse(joinPoint,"删除");
    }

    /**
     *  添加日志复用
     * @param joinPoint
     * @param tip
     */
    public void reuse(JoinPoint joinPoint,String tip) {
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object[] objects=joinPoint.getArgs();
        OptLog log = new OptLog();
        log.setLoginIp(request.getRemoteAddr());
        log.setMsUserId(UserController.userController.findUser().getMsUserId());
        String[] strings = objects[0].toString().split(",");
        Integer length = strings.length;
        for (int i= 0; i < length;i++
             ) {
            if ((strings[i].contains("LogContent"))) {
                String content = strings[i].replace("LogContent="," ");
                log.setContent("【"+ tip +"】-【"+content+"】");
                break;
            }
        }
//        System.out.println(log.toString());
        logMap.put("log", log);
        if (logService.insert(log) > 0) {
        } else {
            log.setContent(log.getContent()+"【失败】");
            logService.insert(log);
        }
    }

}
