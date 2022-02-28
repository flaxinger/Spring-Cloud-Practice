package com.BankTransactionApp.BankTransactionApp.web.banktransaction.service;

import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.Account.service.AccountService;
import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;
import com.BankTransactionApp.BankTransactionApp.web.bank.service.BankService;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.BankTransactionDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.ResponseDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@SpringBootTest
public class BankTransactionServiceTest {

    @Autowired
    private BankTransactionService bankTransactionService;

    @Autowired
    private BankService bankService;

    @Autowired
    private AccountService accountService;

    @Test
    @Transactional
    void Dynamic_Query(){

        /// Query By Bank ///

        // 아무 인자도 없는 경우
        List<ResponseDto> bankTransactionDtoList = bankTransactionService.findTransactionByBank(null, null, null);
        // 현재 데이터 확인용 로그
        for(ResponseDto r : bankTransactionDtoList)
            log.info(r.toString());

        // 은행만 있는 경우
        Assertions.assertThat(bankTransactionService.findTransactionByBank("9999L", null, null).size()).isEqualTo(2);
        // 은행 거래타입만 있는 경우
        Assertions.assertThat(bankTransactionService.findTransactionByBank("9999L", null, TransactionType.DEPOSIT).size()).isEqualTo(2);
        // 은행 거래일만 있는 경우
        Assertions.assertThat(bankTransactionService.findTransactionByBank("8888L", LocalDate.of(2022, 1,1), null).size()).isEqualTo(1);
        // 거래타입 거래일만 있는 경우
        Assertions.assertThat(bankTransactionService.findTransactionByBank(null, LocalDate.of(2022, 1,1), TransactionType.WITHDRAW).size()).isEqualTo(1);

        // 유저만 있는 경우
        Assertions.assertThat(bankTransactionService.findTransactionByUser(1000001L, null, null).size()).isEqualTo(1);
        // 모든 값이 있는 경우
        Assertions.assertThat(bankTransactionService.findTransactionByUser(1000000L, LocalDate.of(2022,1,1), TransactionType.DEPOSIT).size()).isEqualTo(2);
        // 날짜가 누락된 경우
        Assertions.assertThat(bankTransactionService.findTransactionByUser(1000000L, null, TransactionType.DEPOSIT).size()).isEqualTo(2);
        // 거래 유형이 누락된 경우
        Assertions.assertThat(bankTransactionService.findTransactionByUser(1000000L, LocalDate.of(2022,1,1), null).size()).isEqualTo(2);

    }

    @BeforeEach
    void init(){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        String[] bankId = {"9999L", "8888L"};
        String[] bankName = {"목뱅크", "요한뱅크"};

        Long[] accountid = {1000000L, 1000001L};
        String[] accountName = {"목요한", "요한목"};

        Long transactionId = 1000000L;
        LocalDate[] date = {LocalDate.parse("2022-01-01", formatter),
                LocalDate.parse("2022-01-01", formatter),
                LocalDate.parse("2022-02-02", formatter)};
        Long[] transactionAmount = {10L, 20L, 30L};
        TransactionType[] transactionTypes = {TransactionType.DEPOSIT, TransactionType.WITHDRAW, TransactionType.DEPOSIT};

        Set<AccountDto> accountDtoSet = new HashSet<>();
        Set<BankTransactionDto> bankTransactionDtoSet = new HashSet<>();

        for(int i =0;i < 2; i++){
            Assertions.assertThat(bankService.findById(bankId[i]).isEmpty());
            bankService.addBank(BankDto.builder()
                    .bankCode(bankId[i])
                    .bankName(bankName[i])
                    .build());

            Assertions.assertThat(accountService.findById(accountid[i]).isEmpty());
            accountDtoSet.add(AccountDto.builder()
                    .id(accountid[i])
                    .name(accountName[i])
                    .build());
        }

        for(int i =0 ; i < 3; i++){
            Assertions.assertThat(bankTransactionService.findById(transactionId+i).isEmpty());
            bankTransactionDtoSet.add(BankTransactionDto.builder()
                    .id(transactionId+i)
                    .date(date[i%2])
                    .account(AccountDto.builder()
                            .id(accountid[i%2])
                            .name(accountName[i%2])
                            .build())
                    .bank(BankDto.builder().bankCode(bankId[i%2]).build())
                    .transactionType(transactionTypes[i])
                    .transactionAmount(transactionAmount[i])
                    .build());
        }
        bankTransactionService.saveCSVBatch(accountDtoSet, bankTransactionDtoSet);
    }
}
