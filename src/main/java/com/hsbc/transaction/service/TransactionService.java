package com.hsbc.transaction.service;

import com.hsbc.transaction.model.TransactionInfo;
import com.hsbc.transaction.vo.TransactionVO;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * 交易服务
 * @author zpyu
 */
public interface TransactionService {

    /**
     * 创建交易
     *
     * @param transactionInfo
     * @return
     */
    TransactionVO createTransaction(@NonNull TransactionInfo transactionInfo);

    /**
     * 修改交易
     *
     * @param transactionInfo
     * @return
     */
    TransactionVO modifyTransaction(@NonNull TransactionInfo transactionInfo);

    /**
     * 删除交易
     *
     * @param transactionId
     */
    TransactionVO deleteTransaction(@NonNull String transactionId);

    /**
     * 查询交易
     *
     * @param transactionId
     * @return
     */
    TransactionVO getTransaction(@NonNull String transactionId);


    /**
     * 查询所有交易
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<TransactionVO> listAllTransactions(@NonNull Integer pageNo, @NonNull Integer pageSize);
}
