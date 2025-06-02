package com.hsbc.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.transaction.dto.TransactionRequest;
import com.hsbc.transaction.model.TransactionInfo;
import com.hsbc.transaction.service.TransactionService;
import com.hsbc.transaction.vo.TransactionVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateTransaction_success() throws Exception {
        // Arrange
        TransactionInfo info = new TransactionInfo();
        info.setDescription("create info");

        TransactionRequest request = new TransactionRequest();
        request.setTransactionInfo(info);

        TransactionVO vo = new TransactionVO();
        vo.setTransactionId("T2025060001");
        vo.setDescription("create info");

        when(transactionService.createTransaction(any(TransactionInfo.class))).thenReturn(vo);

        // Act & Assert
        mockMvc.perform(post("/transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.transactionId", notNullValue()))
                .andExpect(jsonPath("$.data.description", equalTo("create info")))
                .andDo(print());

        verify(transactionService, times(1)).createTransaction(any(TransactionInfo.class));
    }

    @Test
    void testUpdateTransaction_success() throws Exception {
        // Arrange
        TransactionInfo info = new TransactionInfo();
        info.setTransactionId("T2025060001");
        info.setDescription("updated info");

        TransactionRequest request = new TransactionRequest();
        request.setTransactionInfo(info);

        TransactionVO vo = new TransactionVO();
        vo.setTransactionId("T2025060001");
        vo.setDescription("updated info");

        when(transactionService.modifyTransaction(any(TransactionInfo.class))).thenReturn(vo);

        // Act & Assert
        mockMvc.perform(post("/transactions/modify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.transactionId", equalTo("T2025060001")))
                .andExpect(jsonPath("$.data.description", equalTo("updated info")))
                .andDo(print());

        verify(transactionService, times(1)).modifyTransaction(any(TransactionInfo.class));
    }

    @Test
    void testDeleteTransaction_success() throws Exception {
        // Arrange
        String transactionId = "T2025060001";

        when(transactionService.deleteTransaction(eq(transactionId))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/transactions/delete")
                .param("transactionId", transactionId))
                .andExpect(status().isNoContent())
                .andDo(print());

        verify(transactionService, times(1)).deleteTransaction(eq(transactionId));
    }

    @Test
    void testGetTransaction_found() throws Exception {
        // Arrange
        String transactionId = "T2025060001";

        TransactionVO vo = new TransactionVO();
        vo.setTransactionId(transactionId);
        vo.setDescription("test get info");

        when(transactionService.getTransaction(eq(transactionId))).thenReturn(vo);

        // Act & Assert
        mockMvc.perform(post("/transactions/getOne")
                .param("transactionId", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data.transactionId", equalTo(transactionId)))
                .andExpect(jsonPath("$.data.description", equalTo("test get info")))
                .andDo(print());

        verify(transactionService, times(1)).getTransaction(eq(transactionId));
    }

    @Test
    void testListAllTransactions_hasData() throws Exception {
        // Arrange
        int pageNo = 0;
        int pageSize = 10;

        TransactionVO vo1 = new TransactionVO();
        vo1.setTransactionId("T2025060001");
        vo1.setDescription("item T2025060001");

        TransactionVO vo2 = new TransactionVO();
        vo2.setTransactionId("T2025060002");
        vo2.setDescription("item T2025060002");

        List<TransactionVO> list = Arrays.asList(vo1, vo2);

        when(transactionService.listAllTransactions(eq(pageNo), eq(pageSize))).thenReturn(list);

        // Act & Assert
        mockMvc.perform(post("/transactions/listAll")
                .param("pageNo", String.valueOf(pageNo))
                .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].description", equalTo("item T2025060001")))
                .andExpect(jsonPath("$.data[1].description", equalTo("item T2025060002")))
                .andDo(print());

        verify(transactionService, times(1)).listAllTransactions(eq(pageNo), eq(pageSize));
    }

    @Test
    void testListAllTransactions_emptyList() throws Exception {
        // Arrange
        int pageNo = 0;
        int pageSize = 10;

        when(transactionService.listAllTransactions(eq(pageNo), eq(pageSize))).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(post("/transactions/listAll")
                .param("pageNo", String.valueOf(pageNo))
                .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(HttpStatus.OK.value())))
                .andExpect(jsonPath("$.data", hasSize(0)))
                .andDo(print());

        verify(transactionService, times(1)).listAllTransactions(eq(pageNo), eq(pageSize));
    }
}
