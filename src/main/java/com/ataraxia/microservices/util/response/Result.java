package com.ataraxia.microservices.util.response;

import java.io.Serializable;

/**
 * @author 接口响应对象
 */
public class Result implements Serializable {

    private int code;

    private String msg;

    private Object data;

    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success() {
        return construction(200, "成功", null);
    }

    public static Result success(String msg) {
        return construction(200, msg, null);
    }

    public static Result success(String msg, Object data) {
        return construction(200, msg, data);
    }

    public static Result failure() {
        return construction(500, "失败", null);
    }

    public static Result failure(String msg) {
        return construction(500, msg, null);
    }

    public static Result failure(String msg, Object data) {
        return construction(500, msg, data);
    }

    private static Result construction(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
