package com.BankTransactionApp.BankTransactionApp.web.banktransaction.util;

import java.util.Locale;

public enum TransactionType {
    WITHDRAW, DEPOSIT;

    public static TransactionType of(String transactionType){

        if (transactionType == null) return null;

        transactionType = transactionType.toUpperCase(Locale.ROOT);

        switch (transactionType){
            case("WITHDRAW"):
                return WITHDRAW;
            case("DEPOSIT"):
                return DEPOSIT;
            default:
                throw new IllegalArgumentException(transactionType+"은 지원되지 않는 거래 종류입니다.");
        }
    }
}
