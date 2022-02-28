package com.BankTransactionApp.BankTransactionApp.web.bank.service;

import com.BankTransactionApp.BankTransactionApp.web.bank.domain.Bank;
import com.BankTransactionApp.BankTransactionApp.web.bank.dto.BankDto;

import java.util.List;
import java.util.Optional;

public interface BankService {

    public void addBank(BankDto bankDto);

    public List<Bank> findAllEntity();

    public Optional<BankDto> findById(String id);
}
