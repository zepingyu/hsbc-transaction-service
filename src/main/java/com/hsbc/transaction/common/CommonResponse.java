package com.hsbc.transaction.common;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * @author zpyu
 */
@Data
public class CommonResponse<T> {
    private int code;
    private String msg;
    private T data;

    CommonResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(HttpStatus.OK.value(), "success", data);
    }

    public static <T> CommonResponse<T> error(int code, String msg) {
        return new CommonResponse<>(code, msg, null);
    }
}
