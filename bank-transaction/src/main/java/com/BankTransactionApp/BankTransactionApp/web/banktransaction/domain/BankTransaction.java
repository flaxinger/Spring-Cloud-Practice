package com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain;


import com.BankTransactionApp.BankTransactionApp.web.Account.domain.Account;
import com.BankTransactionApp.BankTransactionApp.web.bank.domain.Bank;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BankTransaction {

    @Id
    @Column(name = "transaction_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_transaction_user"), nullable = false)
    @Setter
    private Account account;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_code", foreignKey = @ForeignKey(name = "fk_transaction_bank"), nullable = false)
    private Bank bank;

    @Column(nullable = false)
    private Long transactionAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

}
