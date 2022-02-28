package com.BankTransactionApp.BankTransactionApp.web.Account.domain;

import com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain.BankTransaction;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankTransaction> bankTransactions = new ArrayList<>();

    public void addBankTransaction(BankTransaction bankTransaction){
        bankTransaction.setAccount(this);
        this.bankTransactions.add(bankTransaction);
    }

}
