package com.springboot.result.common.lang;

import com.springboot.result.common.error.ErrorInfoEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code;

    private String msg;

    private Object data;

    public static Result succ() {
        return succ(200, "操作成功", null);
    }

    public static Result succ(Object data) {
        return succ(200, "操作成功", data);
    }

    public static Result succ(int code, String msg) {
        return succ(code, msg, null);
    }

    public static Result succ(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg) {
        return fail(400, msg, null);
    }

    public static Result fail(int code, String msg) {
        return fail(code, msg, null);
    }

    public static Result fail(ErrorInfoEnum errorInfoEnum) {
        return fail(errorInfoEnum.getCode(), errorInfoEnum.getMessage(), null);
    }

    public static Result fail(ErrorInfoEnum errorInfoEnum, String msg) {
        String Message = String.format(errorInfoEnum.getMessage(), msg);
        return fail(errorInfoEnum.getCode(), Message, null);
    }


    public static Result fail(ErrorInfoEnum errorInfoEnum, Object data) {
        return fail(errorInfoEnum.getCode(), errorInfoEnum.getMessage(), data);
    }

    public static Result fail(int code, String msg, Object data) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

}
