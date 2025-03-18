package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.dtos.CreateAccountDto;
import com.jonathangomz.economy21.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class AccountManager {
    private final AccountRepository accountRepository;

    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account AddAccount(CreateAccountDto dto, String userId) {
        var account = new Account();
        account.setName(dto.getName());
        account.setTotal(dto.getTotal());
        account.setType(dto.getType());
        account.setOwner(userId);

        // TODO: create initial movement to set the total

        return this.accountRepository.save(account);
    }

    public Iterable<Account> getAccounts() {
        return accountRepository.findAllActive();
    }

    public Account getAccount(UUID id) {
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
