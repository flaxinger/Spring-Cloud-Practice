package com.BankTransactionApp.BankTransactionApp.web.Account.service;

import com.BankTransactionApp.BankTransactionApp.web.Account.domain.Account;
import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.Account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void addAccount(AccountDto accountDto) {

        Optional<Account> account = accountRepository.findById(accountDto.getId());

        if(account.isEmpty()){
            accountRepository.save(AccountDto.toEntity(accountDto));
        }
    }

    @Override
    @Transactional
    public List<Account> saveBatch(Set<AccountDto> accountDtoSet) {
        Set<Account> accounts = accountDtoSet.stream().map(accountDto -> AccountDto.toEntity(accountDto)).collect(Collectors.toSet());
        return accountRepository.saveAll(accounts);
    }

    @Override
    @Transactional
    public Optional<AccountDto> findById(Long id){
        Optional<Account> account = accountRepository.findById(id);
        return account.isEmpty() ? Optional.empty() : Optional.of(AccountDto.fromEntity(account.get()));
    }

}
