package com.hsbc.transaction.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zpyu
 */
@Data
public class TransactionVO {
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
     * 交易状态
     */
    private String status;
    /**
     * 交易创建时间
     */
    private String createTime;

    /**
     * 交易更新时间
     */
    private String updateTime;

}
