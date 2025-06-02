package com.hsbc.transaction.enums;

import org.springframework.http.HttpStatus;

/**
 * 响应状态码枚举类
 *
 * @ author zpyu
 */
public enum ResponseStatusCode {

    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Not Found."),
    CONFLICT(HttpStatus.CONFLICT.value(), "Conflict."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "System exception, please contact the administrator.");

    private final int code;
    private final String message;

    private ResponseStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
