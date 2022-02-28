package com.BankTransactionApp.BankTransactionApp.web.bank.repository;

import com.BankTransactionApp.BankTransactionApp.web.bank.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, String> {

}
