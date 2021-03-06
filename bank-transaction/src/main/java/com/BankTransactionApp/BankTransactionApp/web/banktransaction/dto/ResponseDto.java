package com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto;

import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Getter
@AllArgsConstructor
public class ResponseDto {

    private Long transactionId;
    private LocalDate date;
    private Long accountId;
    private String bank;
    private Long transactionAmount;
    private TransactionType transactionType;
}
