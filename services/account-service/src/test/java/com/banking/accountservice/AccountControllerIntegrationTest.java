// ============================================================================
// INTEGRATION TEST - AccountControllerTest.java
// ============================================================================

package com.banking.accountservice;
import com.banking.accountservice.model.dto.CreateAccountRequest;
import com.banking.accountservice.model.entity.Account.AccountType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createAccount_Success() throws Exception {
        // Given
        CreateAccountRequest request = CreateAccountRequest.builder()
                .userId(1L)
                .accountType(AccountType.SAVINGS)
                .currency("USD")
                .initialBalance(BigDecimal.valueOf(1000))
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").exists())
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.accountType").value("SAVINGS"));
    }

    @Test
    void getAccountById_NotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/accounts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void healthCheck_Success() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/accounts/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }
}