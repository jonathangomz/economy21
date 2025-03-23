package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.AccountCreditInformation;
import com.jonathangomz.economy21.model.dtos.AddCreditInformationDto;
import com.jonathangomz.economy21.model.dtos.CreateAccountDto;
import com.jonathangomz.economy21.repository.AccountCreditInformationRepository;
import com.jonathangomz.economy21.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class AccountManager {
    private final AccountRepository accountRepository;
    private final AccountCreditInformationRepository accountCreditInformationRepository;

    public AccountManager(AccountRepository accountRepository, AccountCreditInformationRepository accountCreditInformationRepository) {
        this.accountRepository = accountRepository;
        this.accountCreditInformationRepository = accountCreditInformationRepository;
    }

    // TODO: rename it to addAccount
    public Account AddAccount(CreateAccountDto dto, String userId) {
        var account = new Account();
        account.setName(dto.getName());
        // TODO[linked_2]: remove setTotal
        account.setTotal(BigDecimal.ZERO);
        account.setType(dto.getType());
        account.setOwner(userId);
        return this.accountRepository.save(account);
    }

    public Account addCreditInformation(UUID accountId, AddCreditInformationDto dto) {
        var account = this.getAccount(accountId);

        var creditInformation = new AccountCreditInformation();
        creditInformation.setCreditLimit(dto.getCreditLimit());
        creditInformation.setCutoffDay(dto.getCutoffDay());
        creditInformation.setIntervalPaymentLimit(dto.getIntervalPaymentLimit());

        var saved = this.accountCreditInformationRepository.save(creditInformation);
        account.setCreditInformation(saved);

        return this.accountRepository.save(account);
    }

    // TODO: Should not return any movements
    public Iterable<Account> getAccounts() {
        return accountRepository.findAllActive();
    }

    // TODO: Should return only n number of movements. Max by default 5.
    public Account getAccount(UUID id) {
        // TODO: Implement StreamSupport.stream for better performance
        var accounts = new ArrayList<Account>();
        this.getAccounts().forEach(accounts::add);

        var account = accounts
                .stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();

        if(account.isEmpty()) {
            throw new ResourceNotFound("Account not found");
        }

        return account.get();
    }

    public void RemoveAccount(UUID id) {
        this.accountRepository.deleteById(id);
    }

    public Account updateAccount(UUID id, Account dto) {
        var account = this.getAccount(id);
        account.setName(dto.getName());
        // TODO: Remove when using MovementListeners
        account.setTotal(dto.getTotal());
        account.setType(dto.getType());
        return this.accountRepository.save(account);

    }
}
