package com.hsbc.transaction.enums;

/**
 * 交易状态枚举
 *
 * @author zpyu
 */
public enum TransactionStatus {
    VALID("1", "有效"),
    INVALID("0", "无效");

    private final String code;
    private final String description;

    TransactionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TransactionStatus getByCode(String code) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
