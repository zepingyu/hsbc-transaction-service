package com.hsbc.transaction.dto;

import com.hsbc.transaction.model.TransactionInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author zpyu
 */
@Data

public class TransactionRequest {
    @Valid @NotNull(message = "交易信息不能为空")
    private TransactionInfo transactionInfo;
}
