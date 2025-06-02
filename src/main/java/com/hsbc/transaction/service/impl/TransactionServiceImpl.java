package com.hsbc.transaction.service.impl;

import com.hsbc.transaction.dao.TransactionRepository;
import com.hsbc.transaction.entity.TransactionEntity;
import com.hsbc.transaction.enums.TransactionStatus;
import com.hsbc.transaction.exception.DuplicateTransactionException;
import com.hsbc.transaction.exception.TransactionNotFoundException;
import com.hsbc.transaction.model.TransactionInfo;
import com.hsbc.transaction.service.TransactionService;
import com.hsbc.transaction.vo.TransactionVO;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 交易服务
 *
 * @author zpyu
 */
@CacheConfig(cacheManager = "simpleCacheManager")
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 创建交易记录
     *
     * @param transactionInfo
     * @return
     */
    @Override
    @Caching(
            put = {
                    @CachePut(cacheNames = "transaction", key = "#result.transactionId"),
            },
            evict = {
                    @CacheEvict(cacheNames = "transactionPage", allEntries = true)
            }
    )
    public TransactionVO createTransaction(@NonNull TransactionInfo transactionInfo) {
        //校验是否存在重复交易记录
        if (StringUtils.isNotBlank(transactionInfo.getTransactionId()) && transactionRepository.existsByTransactionId(transactionInfo.getTransactionId())) {
            throw new DuplicateTransactionException("Transaction already exists.");
        }

        TransactionEntity entity = new TransactionEntity();
        BeanUtils.copyProperties(transactionInfo, entity);
        entity.setTransactionId(UUID.randomUUID().toString());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(entity.getCreateTime());
        entity.setStatus(TransactionStatus.VALID.getCode());
        transactionRepository.save(entity);

        return convertToVo(entity);
    }

    /**
     * 修改交易记录
     *
     * @param transactionInfo
     * @return
     */
    @Caching(
            put = {
                    @CachePut(cacheNames = "transaction", key = "#result.transactionId")
            },
            evict = {
                    @CacheEvict(cacheNames = "transactionPage", allEntries = true)
            }
    )
    @Override
    public TransactionVO modifyTransaction(@NonNull TransactionInfo transactionInfo) {
        //校验是否存在该交易记录
        Optional<TransactionEntity> optionalEntity = transactionRepository.findByTransactionId(transactionInfo.getTransactionId());
        if (optionalEntity.isEmpty() || TransactionStatus.INVALID.getCode().equals(optionalEntity.get().getStatus())) {
            throw new TransactionNotFoundException("Transaction not found.");
        }

        TransactionEntity entity = optionalEntity.get();
        BeanUtils.copyProperties(transactionInfo, entity);
        entity.setUpdateTime(LocalDateTime.now());
        transactionRepository.save(entity);

        return convertToVo(entity);
    }

    /**
     * 删除交易记录
     * 注意：假删，缓存更新操作
     *
     * @param transactionId
     */
    @Caching(
            put = {
                    @CachePut(cacheNames = "transaction", key = "#transactionId"),
            },
            evict = {
                    @CacheEvict(cacheNames = "transactionPage", allEntries = true)
            }
    )
    @Override
    public TransactionVO deleteTransaction(@NonNull String transactionId) {
        //校验是否存在该交易记录
        Optional<TransactionEntity> optionalEntity = transactionRepository.findByTransactionId(transactionId);
        if (optionalEntity.isEmpty()) {
            throw new TransactionNotFoundException("Transaction not found.");
        }

        //交易数据假删（无效）
        TransactionEntity entity = optionalEntity.get();
        entity.setStatus(TransactionStatus.INVALID.getCode());
        entity.setUpdateTime(LocalDateTime.now());
        transactionRepository.save(entity);

        return convertToVo(entity);
    }

    /**
     * 查询交易记录
     * 扩展：可定制化查询有效记录
     *
     * @param transactionId
     * @return
     */
    @Cacheable(cacheNames = "transaction", key = "#transactionId")
    @Override
    public TransactionVO getTransaction(@NonNull String transactionId) {
        //校验是否存在交易记录
        Optional<TransactionEntity> optionalEntity = transactionRepository.findByTransactionId(transactionId);
        if (optionalEntity.isEmpty()) {
            throw new TransactionNotFoundException("Transaction not found.");
        }

        return convertToVo(optionalEntity.get());
    }

    /**
     * 查询所有有效交易记录
     * 扩展：可定制化查询所有交易记录（不过滤交易状态）
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Cacheable(cacheNames = "transactionPage", key = "#pageNo + '_' + #pageSize", unless = "#result == null")
    @Override
    public List<TransactionVO> listAllTransactions(@NonNull Integer pageNo, @NonNull Integer pageSize) {
        //查找有效记录并分页返回
        List<TransactionEntity> allTransactions = transactionRepository.findAllValid();
        if (allTransactions == null || allTransactions.isEmpty()) {
            return Collections.emptyList();
        }

        int start = Math.min(pageNo * pageSize, allTransactions.size());
        int end = Math.min(start + pageSize, allTransactions.size());
        allTransactions = allTransactions.subList(start, end);

        return allTransactions.stream().map(this::convertToVo).collect(Collectors.toList());
    }

    private TransactionVO convertToVo(TransactionEntity entity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TransactionVO transactionVO = new TransactionVO();
        BeanUtils.copyProperties(entity, transactionVO);
        transactionVO.setStatus(Objects.requireNonNull(TransactionStatus.getByCode(entity.getStatus())).getDescription());
        transactionVO.setCreateTime((entity.getCreateTime() != null) ? entity.getCreateTime().format(formatter) : "");
        transactionVO.setUpdateTime((entity.getUpdateTime() != null) ? entity.getUpdateTime().format(formatter) : "");

        return transactionVO;
    }
}
