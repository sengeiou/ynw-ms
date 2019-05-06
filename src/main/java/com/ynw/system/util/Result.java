package com.ynw.system.util;

/**
 * 响应的返回结果
 */

public class Result<T> {
    //方法执行状态
    private int code;
    //返回的结果内容
    private T result;
    //返回的提示信息
    private String message;

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", result=" + result +
                ", message='" + message + '\'' +
                '}';
    }

    public Result(int code, T result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
