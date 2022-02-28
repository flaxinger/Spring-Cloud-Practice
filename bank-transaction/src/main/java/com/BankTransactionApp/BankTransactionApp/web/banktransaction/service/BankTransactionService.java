package com.BankTransactionApp.BankTransactionApp.web.banktransaction.service;

import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.BankTransactionDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.dto.ResponseDto;
import com.BankTransactionApp.BankTransactionApp.web.banktransaction.util.TransactionType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BankTransactionService {

    public List<ResponseDto> findTransactionByUser(Long userId, LocalDate localDate, TransactionType transactionType);

    public List<ResponseDto> findTransactionByBank(String bankCode, LocalDate localDate, TransactionType transactionType);

    public String saveCSVBatch(Set<AccountDto> accountDtoSet, Set<BankTransactionDto> bankTransactionDtos);

    public Optional<BankTransactionDto> findById(Long id);
}
