package com.hsbc.transaction.exception;

/**
 * @author zpyu
 */
public class DuplicateTransactionException extends RuntimeException{

    public DuplicateTransactionException(String message) {
        super(message);
    }

}
