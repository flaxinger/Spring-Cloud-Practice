package com.BankTransactionApp.BankTransactionApp.web.banktransaction.repository;

import com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    @Modifying
    @Query(value = "load data concurrent local infile ?1 " +
            "into table bank_transaction " +
            "fields terminated by ',';", nativeQuery = true)
    public int loadData(String path);
}
