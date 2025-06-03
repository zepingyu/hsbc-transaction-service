package com.hsbc.transaction.controller;

import com.hsbc.transaction.common.CommonResponse;
import com.hsbc.transaction.dto.TransactionRequest;
import com.hsbc.transaction.service.TransactionService;
import com.hsbc.transaction.vo.TransactionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zpyu
 */
@RestController
@Tag(name = "交易管理模块", description = "交易相关操作")
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "创建交易记录",description = "创建交易记录，记录状态默认为有效")
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<TransactionVO>> createTransaction(@RequestBody TransactionRequest request) {
        TransactionVO result = transactionService.createTransaction(request.getTransactionInfo());
        return ResponseEntity.ok(CommonResponse.success(result));
    }


    @Operation(summary = "修改交易记录",description = "修改交易记录接口，记录状态不变更")
    @RequestMapping(path = "/modify", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<TransactionVO>> updateTransaction(
            @RequestBody TransactionRequest request) {
        TransactionVO result = transactionService.modifyTransaction(request.getTransactionInfo());
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @Operation(summary = "删除交易记录",description = "删除交易记录，假删操作，记录状态更新成无效")
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse> deleteTransaction(@RequestParam(name = "transactionId") String transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.ok(CommonResponse.success());
    }

    @Operation(summary = "查询单条交易记录",description = "查询单条交易记录，状态为有效或无效均可查询")
    @RequestMapping(path = "/getOne", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<TransactionVO>> getTransaction(@RequestParam(name = "transactionId") String transactionId) {
        TransactionVO result = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @Operation(summary = "批量查询交易记录",description = "分页查询交易记录，默认查询状态为有效的记录")
    @RequestMapping(path = "/listAll", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<List<TransactionVO>>> listAllTransactions(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<TransactionVO> result = transactionService.listAllTransactions(pageNo, pageSize);
        return ResponseEntity.ok(CommonResponse.success(result));
    }
}
