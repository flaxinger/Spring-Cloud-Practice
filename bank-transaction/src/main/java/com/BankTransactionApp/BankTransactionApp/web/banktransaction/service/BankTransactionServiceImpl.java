package com.BankTransactionApp.BankTransactionApp.web.banktransaction.service;

import com.BankTransactionApp.BankTransactionApp.web.Account.domain.Account;
import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.Account.service.AccountService;
import com.BankTransactionApp.BankTransactionApp.web.bank.domain.Bank;
import com.BankTransactionApp.BankTransactionApp.web.bank.service.BankService;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain.BankTransaction;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.BankTransactionDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.ResponseDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.repository.BankTransactionQueryRepository;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankTransactionServiceImpl implements BankTransactionService{

    private final BankTransactionQueryRepository bankTransactionQueryRepository;
    private final AccountService accountService;
    private final BankService bankService;
    private final static String errorMsg = "Error occurred in Bank Transaction Service [%s:] ";

    @Override
    @Transactional
    public List<ResponseDto> findTransactionByBank(String bankCode, LocalDate localDate, TransactionType transactionType) {
        return bankTransactionQueryRepository.findByBankDynamicQuery(bankCode, localDate, transactionType);
    }

    @Override
    @Transactional
    public List<ResponseDto> findTransactionByUser(Long userId, LocalDate localDate, TransactionType transactionType) {
        return bankTransactionQueryRepository.findByUserDynamicQuery(userId, localDate, transactionType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveCSVBatch(Set<AccountDto> accountDtoSet, Set<BankTransactionDto> bankTransactionDtos) {

        // Bank Entity 전체 조회 후 맵에 저장
        List<Bank> banks = bankService.findAll();
        Map<String, Bank> bankMap = new HashMap<>();

        banks.forEach(bank -> {
            bankMap.putIfAbsent(bank.getId(), bank);
        });

        log.info(bankMap.toString());

        // 유저 전체 Insert 후 맵에 저장
        Map<Long, Account> accountMap = new HashMap<>();

        List<Account> accountDtos = accountService.saveBatch(accountDtoSet);
        accountDtos.forEach(account -> {
            accountMap.putIfAbsent(account.getId(), account);
        });


        // 트랜잭션을 은행과 유저에 매핑
        bankTransactionDtos.stream().forEach(bankTransactionDto -> {
            if(bankMap.get(bankTransactionDto.getBank().getBankCode())!=null){
                BankTransaction bankTransaction = BankTransactionDto.toEntity(bankTransactionDto);
                bankMap.get(bankTransactionDto.getBank().getBankCode()).addTransaction(bankTransaction);
                accountMap.get(bankTransactionDto.getAccount().getId()).addBankTransaction(bankTransaction);
            }
            else{
                log.error("there is no bank with bankcode "+bankTransactionDto.getBank().toString());
            }

        });

        return "All transactions were successfully inserted";
    }


    public String fallbackSaveCSVBatch(Set<AccountDto> accountDtoSet, Set<BankTransactionDto> bankTransactionDtos, Throwable t) {
        log.error(String.format(errorMsg, "saveCSVBatch(...)"), t);
        return "Error occurred in uploading csv file.";
    }
}
