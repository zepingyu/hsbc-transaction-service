package com.hsbc.transaction.service.impl;

import com.hsbc.transaction.dao.TransactionRepository;
import com.hsbc.transaction.entity.TransactionEntity;
import com.hsbc.transaction.enums.TransactionStatus;
import com.hsbc.transaction.enums.TransactionType;
import com.hsbc.transaction.exception.DuplicateTransactionException;
import com.hsbc.transaction.exception.TransactionNotFoundException;
import com.hsbc.transaction.model.TransactionInfo;
import com.hsbc.transaction.vo.TransactionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * author zpyu
 */
@SpringBootTest
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //createTransaction
    @Test
    void testCreateTransaction_duplicateId_throwsException() {
        TransactionInfo info = new TransactionInfo();
        String id = "T202506001";
        info.setTransactionId(id);

        when(transactionRepository.existsByTransactionId(id)).thenReturn(true);

        assertThatThrownBy(() -> transactionService.createTransaction(info))
                .isInstanceOf(DuplicateTransactionException.class)
                .hasMessage("Transaction already exists.");
    }

    @Test
    void testCreateTransaction_successfullyCreated() {
        TransactionInfo info = new TransactionInfo();
        info.setAmount(new BigDecimal("100.0"));
        info.setDescription("Create deposit Test");

        TransactionEntity savedEntity = new TransactionEntity();
        savedEntity.setTransactionId(UUID.randomUUID().toString());
        savedEntity.setType(TransactionType.DEPOSIT.getCode());
        savedEntity.setAmount(new BigDecimal("100.0"));
        savedEntity.setDescription("Create deposit Test");
        savedEntity.setStatus(TransactionStatus.VALID.getCode());
        savedEntity.setCreateTime(LocalDateTime.now());
        savedEntity.setUpdateTime(savedEntity.getCreateTime());

        when(transactionRepository.existsByTransactionId(anyString())).thenReturn(false);
        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(savedEntity);

        TransactionVO result = transactionService.createTransaction(info);

        assertThat(result).isNotNull();
        assertThat(result.getAmount()).isEqualTo(savedEntity.getAmount());
        assertThat(result.getDescription()).isEqualTo(savedEntity.getDescription());
        verify(transactionRepository, times(1)).save(any(TransactionEntity.class));
    }

    //modifyTransaction
    @Test
    void testModifyTransaction_notFound_throwsException() {
        TransactionInfo info = new TransactionInfo();
        info.setTransactionId("T202506001");

        when(transactionRepository.findByTransactionId("T202506001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.modifyTransaction(info))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessage("Transaction not found.");
    }

    @Test
    void testModifyTransaction_invalidStatus_throwsException() {
        TransactionInfo info = new TransactionInfo();
        info.setTransactionId("T202506001");

        TransactionEntity entity = new TransactionEntity();
        entity.setStatus(TransactionStatus.INVALID.getCode());

        when(transactionRepository.findByTransactionId("T202506001")).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> transactionService.modifyTransaction(info))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessage("Transaction not found.");
    }

    @Test
    void testModifyTransaction_successfullyUpdated() {
        TransactionInfo info = new TransactionInfo();
        info.setTransactionId("T202506001");
        info.setDescription("Updated Desc");

        TransactionEntity original = new TransactionEntity();
        original.setTransactionId("T202506001");
        original.setDescription("Old Desc");
        original.setStatus(TransactionStatus.VALID.getCode());
        original.setCreateTime(LocalDateTime.now());

        when(transactionRepository.findByTransactionId("T202506001")).thenReturn(Optional.of(original));
        when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionVO result = transactionService.modifyTransaction(info);

        assertThat(result.getDescription()).isEqualTo("Updated Desc");
        verify(transactionRepository, times(1)).save(original);
    }

    //deleteTransaction
    @Test
    void testDeleteTransaction_notFound_throwsException() {
        when(transactionRepository.findByTransactionId("T202506001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.deleteTransaction("T202506001"))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessage("Transaction not found.");
    }

    @Test
    void testDeleteTransaction_successfullyDeleted() {
        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionId("T202506001");
        entity.setStatus(TransactionStatus.VALID.getCode());
        entity.setCreateTime(LocalDateTime.now());

        when(transactionRepository.findByTransactionId("T202506001")).thenReturn(Optional.of(entity));
        when(transactionRepository.save(any(TransactionEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransactionVO result = transactionService.deleteTransaction("T202506001");

        assertThat(result.getStatus()).isEqualTo(TransactionStatus.INVALID.getDescription());
        assertThat(entity.getStatus()).isEqualTo(TransactionStatus.INVALID.getCode());
    }

    //getTransaction
    @Test
    void testGetTransaction_notFound_throwsException() {
        when(transactionRepository.findByTransactionId("T202506001")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransaction("T123"))
                .isInstanceOf(TransactionNotFoundException.class)
                .hasMessage("Transaction not found.");
    }

    @Test
    void testGetTransaction_successfullyReturned() {
        TransactionEntity entity = new TransactionEntity();
        entity.setTransactionId("T202506001");
        entity.setStatus(TransactionStatus.VALID.getCode());
        entity.setCreateTime( LocalDateTime.now());

        when(transactionRepository.findByTransactionId("T202506001")).thenReturn(Optional.of(entity));

        TransactionVO result = transactionService.getTransaction("T202506001");

        assertThat(result).isNotNull();
        assertThat(result.getTransactionId()).isEqualTo("T202506001");
    }

    //listAllTransactions
    @Test
    void testListAllTransactions_emptyList_returnsEmpty() {
        when(transactionRepository.findAllValid()).thenReturn(Collections.emptyList());

        List<TransactionVO> result = transactionService.listAllTransactions(0, 10);

        assertThat(result).isEmpty();
    }

    @Test
    void testListAllTransactions_withData_returnsPagedList() {
        List<TransactionEntity> entities = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            TransactionEntity entity = new TransactionEntity();
            entity.setTransactionId("T" + i);
            entity.setStatus(TransactionStatus.VALID.getCode());
            entities.add(entity);
        }

        when(transactionRepository.findAllValid()).thenReturn(entities);

        List<TransactionVO> result = transactionService.listAllTransactions(1, 10); // page 1, size 10

        assertThat(result).hasSize(5);
        assertThat(result.get(0).getTransactionId()).isEqualTo("T10");
    }
}