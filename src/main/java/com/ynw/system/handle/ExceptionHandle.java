package com.ynw.system.handle;

import com.ynw.system.enums.ResultEnums;
import com.ynw.system.exception.MyException;
import com.ynw.system.util.Result;
import com.ynw.system.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 异常捕获
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        logger.error("【系统错误】{}", e);
        //判断是否是自己设置的异常
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            return new Result(myException.getCode(), myException.getMessage());
        } else {
            if (e instanceof DuplicateKeyException) {
                return ResultUtil.error(ResultEnums.DATA_DUPLICATION_ERR0R);
            } else if (e instanceof DataIntegrityViolationException) {
                return ResultUtil.error(ResultEnums.ADDITION_FAILED);
            } else if (e instanceof IOException) {
                return ResultUtil.error(ResultEnums.IO_ERROR);
            }
//            logger.error("【系统错误】{}", e);
            return ResultUtil.error(ResultEnums.UNKNOWN_ERROR);
        }
    }
}

