package com.BankTransactionApp.BankTransactionApp.web.banktransaction.repository;

import com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

}
