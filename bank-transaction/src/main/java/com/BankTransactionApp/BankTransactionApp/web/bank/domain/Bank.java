package com.BankTransactionApp.BankTransactionApp.web.bank.domain;

import com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain.BankTransaction;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Bank implements Persistable<String> {

    @Id
    private String id;

    @Column
    private String bankName;

    @Builder.Default
    @OneToMany(mappedBy = "bank",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<BankTransaction> bankTransactions = new ArrayList<>();

    public void addTransaction(BankTransaction bankTransaction){
        bankTransaction.setBank(this);
        this.bankTransactions.add(bankTransaction);
    }

    @Override
    public boolean isNew() {
        return true;
    }

    @Override
    public String toString(){ return "[BankId = "+this.id+", BankName = "+this.bankName+"]";}
}
