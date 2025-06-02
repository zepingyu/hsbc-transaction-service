package com.hsbc.transaction.entity;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zpyu
 */
@Data
public class TransactionEntity {
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
    /**
     * 交易状态:  0-无效  1-有效
     */
    private String status;

    /**
     * 交易创建时间
     */
    private LocalDateTime createTime;

    /**
     * 交易更新时间
     */
    private LocalDateTime updateTime;

}
