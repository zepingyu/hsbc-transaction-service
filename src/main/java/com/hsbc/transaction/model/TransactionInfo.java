package com.hsbc.transaction.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author zpyu
 */
@Data
@Schema(description = "交易基本信息")
public class TransactionInfo {

    @Schema(description = "交易ID", required = false)
    private String transactionId;

    @Schema(description = "账户ID", required = true)
    private String accountId;

    @Schema(description = "交易类型", required = true)
    private String type;

    @Schema(description = "交易金额", required = true)
    private BigDecimal amount;

    @Schema(description = "交易描述", required = false)
    private String description;

}
