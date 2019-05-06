package com.ynw.system.util;

import com.ynw.system.enums.ResultEnums;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * http返回数据工具类
 */
public class ResultUtil {

    public static Result success(Object object){
        Result result = new Result();
        result.setCode(1);
        result.setMessage("成功");
        result.setResult(object);
        return result;
    }

    public static Result success(){
        return success(null);
    }

    public static Result error(ResultEnums enums, Object object){
        Result result = new Result();
        result.setCode(enums.getCode());
        result.setMessage(enums.getMessage());
        result.setResult(object);
        return result;
    }

    public static Result error(ResultEnums enums) {
        return error(enums,null);
    }



    public static ResponseEntity errorResponse(ResultEnums enums, Object object){
        return new ResponseEntity(error(enums, object), HttpStatus.OK);
    }

    public static ResponseEntity errorResponse(ResultEnums enums){
        return new ResponseEntity(error(enums), HttpStatus.OK);
    }

    public static ResponseEntity successResponse(Object object){
        return new ResponseEntity(success(object), HttpStatus.OK);
    }

    public static ResponseEntity successResponse(){
        return new ResponseEntity(success(), HttpStatus.OK);
    }

}
