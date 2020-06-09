package com.iluwa.accountmanagement.jpa.repository;

import com.iluwa.accountmanagement.jpa.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
