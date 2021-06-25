package com.springboot.result.common.error;

public enum ErrorInfoEnum implements ErrorInfoInterface {

    ERROR_A40001("ERROR", 40001, "缺少必要的参数:%s"),
    ERROR_A40002("ERROR", 40002, "%s不存在"),
    ERROR_A40003("ERROR", 40003, "%s更新或保存失败"),
    ERROR_A40004("ERROR", 40004, "%s失败"),
    ERROR_A50001("ERROR", 50001, "权限认证失败");

    private String Result;

    private Integer Code;

    private String Message;

    ErrorInfoEnum(String Result, Integer Code, String Message) {
        this.Result = Result;
        this.Code = Code;
        this.Message = Message;

    }

    @Override
    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    @Override
    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        Code = code;
    }

    @Override
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
