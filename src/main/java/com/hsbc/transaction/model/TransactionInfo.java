package com.hsbc.transaction.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zpyu
 */
@Data
public class TransactionInfo {
    /**
     * 交易ID
     */
    private String transactionId;
    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 交易类型
     */
    private String type;
    /**
     * 交易金额
     */
    private BigDecimal amount;
    /**
     * 交易描述
     */
    private String description;

}
