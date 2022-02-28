package com.BankTransactionApp.BankTransactionApp.web.bank.service;

import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class BankServiceTest {

    @Autowired
    private BankService bankService;

    @Test
    @Transactional
    void 입력_조회(){

        int size = bankService.findAllEntity().size();

        Assertions.assertThat(bankService.findById("999").isEmpty());

        bankService.addBank(BankDto.builder()
                .bankCode("999")
                .bankName("요한은행")
                .build());

        Assertions.assertThat(bankService.findById("999").isPresent());
        Assertions.assertThat(bankService.findAllEntity().size()).isEqualTo(size+1);
    }
}
