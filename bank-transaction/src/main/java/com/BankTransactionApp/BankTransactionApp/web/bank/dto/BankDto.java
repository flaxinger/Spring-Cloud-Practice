package com.BankTransactionApp.BankTransactionApp.web.bank.dto;

import com.BankTransactionApp.BankTransactionApp.web.bank.domain.Bank;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BankDto {

    private String bankCode;
    private String bankName;

    public static BankDto fromEntity(Bank bank){
        return BankDto.builder()
                .bankCode(bank.getId())
                .bankName(bank.getBankName())
                .build();
    }

    public static Bank toEntity(BankDto bankDto){
        return Bank.builder()
                .id(bankDto.getBankCode())
                .bankName(bankDto.getBankName())
                .build();
    }
}
