package com.hsbc.transaction.controller;

import com.hsbc.transaction.common.CommonResponse;
import com.hsbc.transaction.dto.TransactionRequest;
import com.hsbc.transaction.service.TransactionService;
import com.hsbc.transaction.vo.TransactionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zpyu
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<TransactionVO>> createTransaction(@RequestBody TransactionRequest request) {
        TransactionVO result = transactionService.createTransaction(request.getTransactionInfo());
        return ResponseEntity.ok(CommonResponse.success(result));
    }


    @RequestMapping(path = "/modify", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<TransactionVO>> updateTransaction(
            @RequestBody TransactionRequest request) {
        TransactionVO result = transactionService.modifyTransaction(request.getTransactionInfo());
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteTransaction(@RequestParam(name = "transactionId") String transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "/getOne", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<TransactionVO>> getTransaction(@RequestParam(name = "transactionId") String transactionId) {
        TransactionVO result = transactionService.getTransaction(transactionId);
        return ResponseEntity.ok(CommonResponse.success(result));
    }

    @RequestMapping(path = "/listAll", method = RequestMethod.POST)
    public ResponseEntity<CommonResponse<List<TransactionVO>>> listAllTransactions(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<TransactionVO> result = transactionService.listAllTransactions(pageNo, pageSize);
        return ResponseEntity.ok(CommonResponse.success(result));
    }
}
