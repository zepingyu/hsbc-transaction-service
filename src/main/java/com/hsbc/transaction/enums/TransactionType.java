package com.hsbc.transaction.enums;

import lombok.Data;

/**
 * 交易类型枚举类
 *
 * @author zpyu
 */

public enum TransactionType {
    DEPOSIT("deposit", "存款"),
    WITHDRAW("withdraw", "提款"),
    TRANSFER("transfer", "转账");

    private final String code;
    private final String description;

    TransactionType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
