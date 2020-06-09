package com.iluwa.accountmanagement.mapper;

import com.iluwa.accountmanagement.dto.AccountDto;
import com.iluwa.accountmanagement.jpa.entity.Account;

public class AccountMapper {

    public static AccountDto toDto(Account account) {
        return AccountDto.builder()
                .id(account.getId())
                .accountNum(account.getAccountNum())
                .amount(account.getCurrency()
                        .getMoneyConverter()
                        .toDecimal(account.getAmount()))
                .userId(account.getUser()
                        .getId())
                .currency(account.getCurrency()
                        .toString())
                .build();
    }
}
