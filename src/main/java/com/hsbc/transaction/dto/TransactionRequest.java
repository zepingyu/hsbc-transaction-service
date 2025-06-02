package com.hsbc.transaction.dto;

import com.hsbc.transaction.model.TransactionInfo;
import lombok.Data;

/**
 * @author zpyu
 */
@Data
public class TransactionRequest {
    private TransactionInfo transactionInfo;
}
