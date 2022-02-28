package com.BankTransactionApp.BankTransactionApp.util;

import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;
import com.BankTransactionApp.BankTransactionApp.web.bank.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final BankService bankService;

    public void run(ApplicationArguments args) {

        try {
            bankService.addBank(BankDto.builder().bankCode("004").bankName("국민은행").build());
            bankService.addBank(BankDto.builder().bankCode("011").bankName("농협은행").build());
            bankService.addBank(BankDto.builder().bankCode("020").bankName("우리은행").build());
            bankService.addBank(BankDto.builder().bankCode("088").bankName("신한은행").build());
            bankService.addBank(BankDto.builder().bankCode("090").bankName("카카오뱅크").build());
        }
        catch (RuntimeException e){
            //
        }
    }
}
