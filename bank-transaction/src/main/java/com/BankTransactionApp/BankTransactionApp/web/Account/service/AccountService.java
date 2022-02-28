package com.BankTransactionApp.BankTransactionApp.web.Account.service;

import com.BankTransactionApp.BankTransactionApp.web.Account.domain.Account;
import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AccountService {

    public void addAccount(AccountDto accountDto);

    public List<Account> saveBatch(Set<AccountDto> accountDtoSet);

    public Optional<AccountDto> findById(Long id);
}
