package com.BankTransactionApp.BankTransactionApp.web.banktransaction.service;

import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.Account.repository.AccountRepository;
import com.BankTransactionApp.BankTransactionApp.web.Account.service.AccountService;
import com.BankTransactionApp.BankTransactionApp.web.bank.domain.Bank;
import com.BankTransactionApp.BankTransactionApp.web.bank.service.BankService;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.domain.BankTransaction;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.BankTransactionDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.ResponseDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.repository.BankTransactionQueryRepository;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.repository.BankTransactionRepository;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankTransactionService{

    @PersistenceContext
    private EntityManager entityManager;

    private final BankTransactionQueryRepository bankTransactionQueryRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final BankService bankService;
    private final BankTransactionRepository bankTransactionRepository;
    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private Long batchSize;

    @Transactional(readOnly = true)
    public List<ResponseDto> findTransactionByBank(String bankCode, LocalDate localDate, TransactionType transactionType) {
        return bankTransactionQueryRepository.findByBankDynamicQuery(bankCode, localDate, transactionType);
    }

    @Transactional(readOnly = true)
    public List<ResponseDto> findTransactionByUser(Long userId, LocalDate localDate, TransactionType transactionType) {
        return bankTransactionQueryRepository.findByUserDynamicQuery(userId, localDate, transactionType);
    }

    @Transactional(rollbackFor = Exception.class)
    public String saveCSVBatch(Set<AccountDto> accountDtoSet, Set<BankTransactionDto> bankTransactionDtos) {

        // Bank Entity 전체 조회 후 맵에 저장
        Map<String, Bank> bankMap = findAllBankTransformToMap();
        accountService.saveBatch(accountDtoSet);
        int count = 0;
        // 트랜잭션을 은행과 유저에 매핑
        for(BankTransactionDto bankTransactionDto: bankTransactionDtos){
            if(bankMap.get(bankTransactionDto.getBank().getBankCode())!=null){
                BankTransaction bankTransaction = BankTransactionDto.toEntity(bankTransactionDto);
                bankMap.get(bankTransactionDto.getBank().getBankCode()).addTransaction(bankTransaction);
                accountRepository.findById(bankTransactionDto.getAccount().getId()).get().addBankTransaction(bankTransaction);
                count++;
                if(count%batchSize==0){
                    log.info("saving and flushing list size is {}", count);
                    entityManager.flush();
                    bankMap.clear();
                    bankMap.putAll(findAllBankTransformToMap());
                }
            }
            else{
                log.error("there is no bank with bankcode "+bankTransactionDto.getBank().toString());
            }

        }


        return "All transactions were successfully inserted";
    }


    public Optional<BankTransactionDto> findById(Long id) {
        Optional<BankTransaction> bankTransaction = bankTransactionRepository.findById(id);
        return bankTransaction.isEmpty() ? Optional.empty() : Optional.of(BankTransactionDto.fromEntity(bankTransaction.get()));
    }

    private Map<String, Bank> findAllBankTransformToMap(){
        List<Bank> banks = bankService.findAllEntity();
        Map<String, Bank> bankMap = new HashMap<>();

        banks.forEach(bank -> {
            bankMap.putIfAbsent(bank.getId(), bank);
        });
        return bankMap;
    }

}
