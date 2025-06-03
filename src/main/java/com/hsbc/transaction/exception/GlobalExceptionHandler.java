package com.hsbc.transaction.exception;

import com.hsbc.transaction.common.CommonResponse;
import com.hsbc.transaction.enums.ResponseStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for TransactionService
 * 异常处理及日志打印
 *
 * @author zpyu
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<CommonResponse> handleTransactionNotFound(TransactionNotFoundException ex) {
        log.warn("TransactionNotFoundException {}", ex.getMessage());
        return ResponseEntity.ok(CommonResponse.error(ResponseStatusCode.NOT_FOUND.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(DuplicateTransactionException.class)
    public ResponseEntity<CommonResponse> handleDuplicateTransaction(DuplicateTransactionException ex) {
        log.warn("DuplicateTransactionException {}", String.valueOf(ex));
        return ResponseEntity.ok(CommonResponse.error(ResponseStatusCode.CONFLICT.getCode(), ex.getMessage()));
    }

    // 处理@Valid/@Validated抛出的校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        log.error("handleValidationException ", ex);
        return ResponseEntity.ok(CommonResponse.error(ResponseStatusCode.BAD_REQUEST.getCode(), ResponseStatusCode.BAD_REQUEST.getMessage(), errors));
    }

    // Add more handlers as needed
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleOtherException(Exception ex) {
        log.error("HandleOtherException ", ex);
        return ResponseEntity.ok(CommonResponse.error(ResponseStatusCode.INTERNAL_SERVER_ERROR.getCode(), ResponseStatusCode.INTERNAL_SERVER_ERROR.getMessage()));
    }

}
