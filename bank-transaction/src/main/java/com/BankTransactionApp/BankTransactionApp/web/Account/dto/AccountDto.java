package com.BankTransactionApp.BankTransactionApp.web.Account.dto;

import com.BankTransactionApp.BankTransactionApp.web.Account.domain.Account;
import lombok.*;

@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class AccountDto {

    private Long id;
    private String name;

    public static AccountDto fromEntity(Account account){
        return AccountDto.builder()
                .id(account.getId())
                .name(account.getName())
                .build();
    }

    public static Account toEntity(AccountDto accountDto){
        return Account.builder()
                .id(accountDto.getId())
                .name(accountDto.getName())
                .build();
    }
}
