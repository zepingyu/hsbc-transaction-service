package com.hsbc.transaction.exception;

import lombok.Data;

/**
 * @author zpyu
 */
@Data
public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
