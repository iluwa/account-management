package com.iluwa.accountmanagement.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountDto {

    private Long id;

    private BigDecimal amount;

    private String accountNum;

    private String currency;

    private Long userId;
}
