package com.BankTransactionApp.BankTransactionApp.web.Account.service;

import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;


    @Test
    @Transactional
    void 입력_조회(){

        Long id = 1000000L;


        Assertions.assertThat(accountService.findById(id).isEmpty());

        AccountDto newAccountDto = AccountDto.builder()
                .id(id)
                .name("목요한")
                .build();

        accountService.addAccount(newAccountDto);

        Assertions.assertThat(accountService.findById(id).isPresent());
    }

    @Test
    @Transactional
    void 벌크_입력(){
        Long id = 1000000L;

        Set<AccountDto> accountDtoSet = new HashSet<>();

        for(int i = 0; i < 5; i++){
            Assertions.assertThat(accountService.findById(id+i).isEmpty());
            accountDtoSet.add(AccountDto.builder()
                    .id(id+i)
                    .name("목요한")
                    .build());
        }

        accountService.saveBatch(accountDtoSet);

        for(int i = 0; i < 5; i++){
            Assertions.assertThat(accountService.findById(id+i).isPresent());
        }
    }
}
