package com.hsbc.transaction.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author zpyu
 */
@Schema(description = "交易记录详细信息")
@Data
public class TransactionVO {

    @Schema(description = "交易ID")
    private String transactionId;

    @Schema(description = "账户ID")
    private String accountId;

    @Schema(description = "交易类型")
    private String type;

    @Schema(description = "交易金额")
    private BigDecimal amount;

    @Schema(description = "交易描述")
    private String description;

    @Schema(description = "交易状态")
    private String status;

    @Schema(description = "交易创建时间")
    private String createTime;

    @Schema(description = "交易更新时间")
    private String updateTime;

}
