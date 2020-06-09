package com.iluwa.accountmanagement.service;

import com.iluwa.accountmanagement.dto.AccountDto;
import com.iluwa.accountmanagement.exception.NotEnoughMoneyException;
import com.iluwa.accountmanagement.jpa.entity.Account;
import com.iluwa.accountmanagement.jpa.entity.Currency;
import com.iluwa.accountmanagement.jpa.entity.User;
import com.iluwa.accountmanagement.jpa.repository.AccountRepository;
import com.iluwa.accountmanagement.jpa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ContextConfiguration(classes = AccountService.class)
class AccountServiceTest extends AbstractDatabaseTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAll() {
        Account account = createSimpleAccount(createUser(), "12345");
        ;

        List<AccountDto> accounts = accountService.findAll();
        assertEquals(1, accounts.size());
        AccountDto saved = accounts.get(0);
        assertEquals(account.getCurrency(), Currency.valueOf(saved.getCurrency()));
        assertEquals(account.getAmount(), account.getCurrency()
                .getMoneyConverter()
                .toLong(saved.getAmount()));
        assertEquals(account.getUser()
                .getId(), saved.getUserId());
        assertEquals(account.getAccountNum(), saved.getAccountNum());
    }

    @Test
    void withdraw_whenNotEnoughAmount() {
        Account account = createSimpleAccount(createUser(), "12345");
        ;
        assertThrows(NotEnoughMoneyException.class,
                () -> accountService.withdraw(account.getId(), BigDecimal.valueOf(999999)));
    }

    @Test
    void withdraw() {
        Account account = createSimpleAccount(createUser(), "12345");
        ;
        accountService.withdraw(account.getId(), BigDecimal.valueOf(10));

        Account updated = accountRepository.findById(account.getId())
                .orElseThrow();
        assertEquals(account.getAmount() - 1000, updated.getAmount());
    }

    @Test
    void withdraw_concurrent() throws InterruptedException {
        int numberOfThreads = 10;
        Account account = createSimpleAccount(createUser(), "12345");
        ;
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch ready = new CountDownLatch(numberOfThreads);
        CountDownLatch done = new CountDownLatch(numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            service.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                accountService.withdraw(account.getId(), BigDecimal.ONE);
                done.countDown();
            });
        }
        ready.await();
        start.countDown();
        done.await();
        Account updated = accountRepository.findById(account.getId())
                .orElseThrow();
        assertEquals(9000, updated.getAmount());
    }

    @Test
    void deposit() {
        Account account = createSimpleAccount(createUser(), "12345");
        accountService.deposit(account.getId(), BigDecimal.valueOf(10));

        Account updated = accountRepository.findById(account.getId())
                .orElseThrow();
        assertEquals(account.getAmount() + 1000, updated.getAmount());
    }

    @Test
    void transfer_notEnoughMoney() {
        Account accountFrom = createSimpleAccount(createUser(), "12345");
        Account accountTo = createSimpleAccount(createUser("User2"), "5545");
        assertThrows(NotEnoughMoneyException.class,
                () -> accountService.transfer(accountFrom.getId(), accountTo.getId(),
                        BigDecimal.valueOf(99999999)));
    }

    @Test
    void transfer() {
        Account accountFrom = createSimpleAccount(createUser(), "12345");
        Account accountTo = createSimpleAccount(createUser("User2"), "5545");
        accountService.transfer(accountFrom.getId(), accountTo.getId(), BigDecimal.ONE);
        Account updatedFrom = accountRepository.findById(accountFrom.getId())
                .orElseThrow();
        Account updatedTo = accountRepository.findById(accountTo.getId())
                .orElseThrow();
        assertEquals(accountFrom.getAmount() - 100, updatedFrom.getAmount());
        assertEquals(accountTo.getAmount() + 100, updatedTo.getAmount());
    }

    private User createUser() {
        User user = new User("user1");
        return userRepository.save(user);
    }

    private User createUser(String login) {
        User user = new User("login");
        return userRepository.save(user);
    }

    private Account createSimpleAccount(User user, String accountNum) {
        Account account = new Account();
        account.setUser(user);
        account.setAccountNum(accountNum);
        account.setCurrency(Currency.RUB);
        account.setAmount(10000L);
        return accountRepository.save(account);
    }
}