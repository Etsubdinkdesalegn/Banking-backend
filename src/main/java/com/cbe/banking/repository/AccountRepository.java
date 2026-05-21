package com.cbe.banking.repository;

import com.cbe.banking.model.Account;
import com.cbe.banking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByUser(User user);
    Optional<Account> findByAccountNumber(String accountNumber);
}
