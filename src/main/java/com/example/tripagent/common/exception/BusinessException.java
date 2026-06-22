package com.example.tripagent.common.exception;

import com.example.tripagent.common.enums.ResponseCodeEnum;

public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException(ResponseCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
    }

    public BusinessException(String message) {
        super(message);
        this.code = ResponseCodeEnum.BUSINESS_ERROR.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}