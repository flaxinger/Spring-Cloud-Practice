package com.BankTransactionApp.BankTransactionApp.web.Account.repository;

import com.BankTransactionApp.BankTransactionApp.web.Account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
