package com.iluwa.accountmanagement.controller;

import com.iluwa.accountmanagement.dto.AccountDto;
import com.iluwa.accountmanagement.dto.TransferResponse;
import com.iluwa.accountmanagement.jpa.entity.Account;
import com.iluwa.accountmanagement.jpa.entity.Currency;
import com.iluwa.accountmanagement.jpa.entity.User;
import com.iluwa.accountmanagement.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {

    }

    @Test
    public void withdraw() throws Exception {
        when(accountService.withdraw(1L, BigDecimal.ONE)).thenReturn(createSimpleAccount());

        mockMvc.perform(post("/accounts/1/withdraw").param("amount", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.accountNum", is("12345")))
                .andExpect(jsonPath("$.currency", is("RUB")))
                .andExpect(jsonPath("$.amount", is(10)));
    }

    @Test
    public void deposit() throws Exception {
        when(accountService.deposit(1L, BigDecimal.ONE)).thenReturn(createSimpleAccount());

        mockMvc.perform(post("/accounts/1/deposit").param("amount", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.accountNum", is("12345")))
                .andExpect(jsonPath("$.currency", is("RUB")))
                .andExpect(jsonPath("$.amount", is(10)));
    }

    @Test
    public void transfer() throws Exception {
        when(accountService.transfer(1L, 2L, BigDecimal.ONE)).thenReturn(TransferResponse.builder()
                .from(createSimpleAccount())
                .to(createSimpleAccount2())
                .build());

        mockMvc.perform(post("/accounts/transfer").param("amount", "1")
                .param("from", "1")
                .param("to", "2")
                .param("amount", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.from.id", is(1)))
                .andExpect(jsonPath("$.from.userId", is(1)))
                .andExpect(jsonPath("$.from.accountNum", is("12345")))
                .andExpect(jsonPath("$.from.currency", is("RUB")))
                .andExpect(jsonPath("$.from.amount", is(10)))
                .andExpect(jsonPath("$.to.id", is(2)))
                .andExpect(jsonPath("$.to.userId", is(2)))
                .andExpect(jsonPath("$.to.accountNum", is("54321")))
                .andExpect(jsonPath("$.to.currency", is("RUB")))
                .andExpect(jsonPath("$.to.amount", is(1)));
    }

    private AccountDto createSimpleAccount() {
        return AccountDto.builder()
                .id(1L)
                .userId(1L)
                .accountNum("12345")
                .currency("RUB")
                .amount(BigDecimal.TEN)
                .build();
    }

    private AccountDto createSimpleAccount2() {
        return AccountDto.builder()
                .id(2L)
                .userId(2L)
                .accountNum("54321")
                .currency("RUB")
                .amount(BigDecimal.ONE)
                .build();
    }
}