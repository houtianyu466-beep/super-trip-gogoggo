package com.example.tripagent.common.enums;

public enum ResponseCodeEnum {

    SUCCESS(200, "成功"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "数据不存在"),
    SYSTEM_ERROR(500, "系统异常"),
    BUSINESS_ERROR(4001, "业务异常"),
    REQUEST_NOT_EXIST(4002, "请求不存在"),
    EMPLOYEE_NOT_EXIST(4003, "员工不存在"),
    RESOURCE_NOT_FOUND(4004, "请求未通过审核");

    private final Integer code;
    private final String message;

    ResponseCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}