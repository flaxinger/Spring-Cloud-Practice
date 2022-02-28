package com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto;

import com.BankTransactionApp.BankTransactionApp.web.Account.domain.Account;
import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.bank.domain.Bank;
import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain.BankTransaction;

import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankTransactionDto {

    private Long id;
    private LocalDate date;
    private AccountDto account;
    private BankDto bank;
    private Long transactionAmount;
    private TransactionType transactionType;

    public static BankTransactionDto fromEntity(BankTransaction bankTransaction){
        return BankTransactionDto.builder()
                .id(bankTransaction.getId())
                .date(bankTransaction.getDate())
                .account(AccountDto.fromEntity(bankTransaction.getAccount()))
                .bank(BankDto.fromEntity(bankTransaction.getBank()))
                .transactionAmount(bankTransaction.getTransactionAmount())
                .transactionType(bankTransaction.getTransactionType())
                .build();
    }

    public static BankTransaction toEntity(BankTransactionDto bankTransactionDto){
        return BankTransaction.builder()
                .id(bankTransactionDto.getId())
                .date(bankTransactionDto.getDate())
                .account(Account.builder().build())
                .transactionAmount(bankTransactionDto.getTransactionAmount())
                .bank(Bank.builder().build())
                .transactionType(bankTransactionDto.getTransactionType())
                .build();
    }

    public static List<BankTransactionDto> fromEntityList(List<BankTransaction> bankTransactions){
        return bankTransactions.stream().map(bankTransaction -> fromEntity(bankTransaction)).collect(Collectors.toList());
    }

    public static List<BankTransaction> toEntityList(List<BankTransactionDto> bankTransactionDtos){
        return bankTransactionDtos.stream().map(bankTransactionDto -> toEntity(bankTransactionDto)).collect(Collectors.toList());
    }
}
