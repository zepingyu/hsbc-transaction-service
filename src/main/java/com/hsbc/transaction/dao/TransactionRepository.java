package com.hsbc.transaction.dao;

import com.hsbc.transaction.entity.TransactionEntity;
import com.hsbc.transaction.enums.TransactionStatus;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zpyu
 */
@Repository
public class TransactionRepository {
    private final Map<String, TransactionEntity> transactions = new ConcurrentHashMap<>();

    public TransactionEntity save(TransactionEntity transaction) {
        transactions.put(transaction.getTransactionId(), transaction);
        return transaction;
    }

    public List<TransactionEntity> findAllValid() {
        return transactions.values().stream().filter(t -> t.getStatus().equals(TransactionStatus.VALID.getCode()))
                .sorted((t1, t2) -> t2.getUpdateTime().compareTo(t1.getUpdateTime())).collect(Collectors.toList());
    }


    public Optional<TransactionEntity> findByTransactionId(String transactionId) {
        return Optional.ofNullable(transactions.get(transactionId));
    }

    public boolean existsByTransactionId(String transactionId) {
        return transactions.containsKey(transactionId);
    }

    public void deleteById(String transactionId) {
        transactions.remove(transactionId);
    }
}
