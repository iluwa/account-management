package com.iluwa.accountmanagement.configuration;


import com.iluwa.accountmanagement.jpa.entity.Account;
import com.iluwa.accountmanagement.jpa.entity.Currency;
import com.iluwa.accountmanagement.jpa.entity.User;
import com.iluwa.accountmanagement.jpa.repository.AccountRepository;
import com.iluwa.accountmanagement.jpa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("demo")
@Slf4j
public class DemoDataInitialization {
    @Bean
    public CommandLineRunner commandLineRunner(AccountRepository accountRepository,
                                               UserRepository userRepository) {
        return args -> {
            log.info("Initializing demo data");
            accountRepository.deleteAll();
            userRepository.deleteAll();

            User user1 = new User("user1");
            User user2 = new User("user2");
            userRepository.save(user1);
            userRepository.save(user2);

            Account account = new Account();
            account.setUser(user1);
            account.setAccountNum("45678");
            account.setCurrency(Currency.RUB);
            account.setAmount(10000L);
            accountRepository.save(account);

            Account account2 = new Account();
            account2.setUser(user2);
            account2.setAccountNum("12345");
            account2.setCurrency(Currency.RUB);
            account2.setAmount(5000L);
            accountRepository.save(account2);
        };
    }
}
