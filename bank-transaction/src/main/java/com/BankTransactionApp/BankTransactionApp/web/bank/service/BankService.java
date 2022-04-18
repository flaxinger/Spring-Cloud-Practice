package com.BankTransactionApp.BankTransactionApp.web.bank.service;

import com.BankTransactionApp.BankTransactionApp.web.bank.domain.Bank;
import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;
import com.BankTransactionApp.BankTransactionApp.web.bank.repository.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankService{

    private final BankRepository bankRepository;

    @Transactional
    public void addBank(BankDto bankDto) {

        Optional<Bank> bank = bankRepository.findById(bankDto.getBankCode());
        if(bank.isEmpty()){
            bankRepository.save(BankDto.toEntity(bankDto));
        }
    }

    @Transactional
    public List<Bank> findAllEntity() {
        return bankRepository.findAll();
    }

    @Transactional
    public Optional<BankDto> findById(String id) {
        Optional<Bank> bank = bankRepository.findById(id);
        return bank.isEmpty() ? Optional.empty() : Optional.of(BankDto.fromEntity(bank.get()));
    }
}
