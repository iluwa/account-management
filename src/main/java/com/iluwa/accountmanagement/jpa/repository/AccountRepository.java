package com.iluwa.accountmanagement.jpa.repository;

import com.iluwa.accountmanagement.jpa.entity.Account;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select acc from Account acc where id = ?1")
    Account lockAccountById(Long id);
}
