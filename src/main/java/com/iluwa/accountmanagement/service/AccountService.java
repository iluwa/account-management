package com.iluwa.accountmanagement.service;

import com.iluwa.accountmanagement.dto.AccountDto;
import com.iluwa.accountmanagement.dto.TransferResponse;
import com.iluwa.accountmanagement.exception.NotEnoughMoneyException;
import com.iluwa.accountmanagement.exception.RecordNotFoundException;
import com.iluwa.accountmanagement.jpa.entity.Account;
import com.iluwa.accountmanagement.jpa.repository.AccountRepository;
import com.iluwa.accountmanagement.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    @SneakyThrows
    public AccountDto withdraw(Long accountId, BigDecimal amount) {
        Account account = lockAccountById(accountId);
        Long amountToWithdraw = account.getCurrency()
                .getMoneyConverter()
                .toLong(amount);
        if (account.getAmount() < amountToWithdraw) {
            throw new NotEnoughMoneyException("На счету не хватает средств для снятия");
        }
        account.setAmount(account.getAmount() - amountToWithdraw);
        return AccountMapper.toDto(accountRepository.save(account));
    }

    @Transactional
    public AccountDto deposit(Long accountId, BigDecimal amount) {
        Account account = lockAccountById(accountId);
        Long amountToWithdraw = account.getCurrency()
                .getMoneyConverter()
                .toLong(amount);
        ;
        account.setAmount(account.getAmount() + amountToWithdraw);
        return AccountMapper.toDto(accountRepository.save(account));
    }

    @Transactional
    public TransferResponse transfer(Long accountIdFrom, Long accountIdTo, BigDecimal amount) {
        Account accountFrom = lockAccountById(accountIdFrom);
        Long amountToTransfer = accountFrom.getCurrency()
                .getMoneyConverter()
                .toLong(amount);
        log.info("Transaction {} -> {}. Amount to transfer: {}", accountIdFrom, accountIdTo,
                amountToTransfer);
        if (accountFrom.getAmount() < amountToTransfer) {
            throw new NotEnoughMoneyException("На счету не хватает средств для снятия");
        }
        Account accountTo = lockAccountById(accountIdTo);
        accountFrom.setAmount(accountFrom.getAmount() - amountToTransfer);
        accountTo.setAmount(accountTo.getAmount() + amountToTransfer);
        return TransferResponse.builder()
                .from(AccountMapper.toDto(accountRepository.save(accountFrom)))
                .to(AccountMapper.toDto(accountRepository.save(accountTo)))
                .build();
    }

    private Account lockAccountById(Long accountId) {
        Objects.requireNonNull(accountId, "Идентификатор счета должен быть заполнен");
        if (!accountRepository.existsById(accountId)) {
            throw new RecordNotFoundException("Счет с идентификатором " + accountId + " не найден");
        }
        return accountRepository.lockAccountById(accountId);
    }

    public List<AccountDto> findAll() {
        return StreamSupport.stream(accountRepository.findAll()
                .spliterator(), false)
                .map(AccountMapper::toDto)
                .collect(Collectors.toList());
    }
}
