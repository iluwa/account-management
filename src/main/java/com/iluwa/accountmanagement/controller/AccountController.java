package com.iluwa.accountmanagement.controller;

import com.iluwa.accountmanagement.dto.AccountDto;
import com.iluwa.accountmanagement.dto.TransferResponse;
import com.iluwa.accountmanagement.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountDto> findAll() {
        return accountService.findAll();
    }

    @PostMapping("/{accountId}/withdraw")
    public AccountDto withdraw(@PathVariable("accountId") Long accountId,
                               @RequestParam("amount") BigDecimal amountToWithdraw) {
        return accountService.withdraw(accountId, amountToWithdraw);
    }

    @PostMapping("/{accountId}/deposit")
    public AccountDto deposit(@PathVariable("accountId") Long accountId,
                              @RequestParam("amount") BigDecimal amountToDeposit) {
        return accountService.deposit(accountId, amountToDeposit);
    }

    @PostMapping("/transfer")
    public TransferResponse transfer(@RequestParam("from") Long accountIdFrom,
                                     @RequestParam("to") Long accountIdTo,
                                     @RequestParam("amount") BigDecimal amount) {
        return accountService.transfer(accountIdFrom, accountIdTo, amount);
    }
}
