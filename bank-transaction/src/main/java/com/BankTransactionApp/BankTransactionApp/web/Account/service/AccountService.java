package com.BankTransactionApp.BankTransactionApp.web.Account.service;

import com.BankTransactionApp.BankTransactionApp.web.Account.domain.Account;
import com.BankTransactionApp.BankTransactionApp.web.Account.dto.AccountDto;
import com.BankTransactionApp.BankTransactionApp.web.Account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public void addAccount(AccountDto accountDto) {

        Optional<Account> account = accountRepository.findById(accountDto.getId());

        if(account.isEmpty()){
            accountRepository.save(AccountDto.toEntity(accountDto));
        }
    }

    @Transactional
    public List<AccountDto> saveBatch(Set<AccountDto> accountDtoSet) {
        Set<Account> accounts = accountDtoSet.stream()
                .map( accountDto -> {
                    if (accountRepository.findById(accountDto.getId()).isEmpty())
                        return AccountDto.toEntity(accountDto);
                    else return null;
                })
                .filter(accountDto -> accountDto!=null)
                .collect(Collectors.toSet());
        List<AccountDto> accountDtos = accountRepository.saveAll(accounts).stream().map(AccountDto::fromEntity).collect(Collectors.toList());
        accountRepository.flush();
        return accountDtos;
    }

    @Transactional
    public Optional<AccountDto> findById(Long id){
        Optional<Account> account = accountRepository.findById(id);
        return account.isEmpty() ? Optional.empty() : Optional.of(AccountDto.fromEntity(account.get()));
    }

}
