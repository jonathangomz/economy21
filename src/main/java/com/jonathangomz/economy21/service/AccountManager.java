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
import java.util.UUID;

@Service
public class AccountManager {
    private final AccountRepository accountRepository;
    private final AccountCreditInformationRepository accountCreditInformationRepository;

    public AccountManager(AccountRepository accountRepository, AccountCreditInformationRepository accountCreditInformationRepository) {
        this.accountRepository = accountRepository;
        this.accountCreditInformationRepository = accountCreditInformationRepository;
    }

    public Account createAccount(UUID owner, CreateAccountDto dto) {
        var account = new Account();
        account.setName(dto.getName());
        // TODO[linked_2]: remove setTotal
        account.setTotal(BigDecimal.ZERO);
        account.setType(dto.getType());
        account.setOwner(owner);
        return this.accountRepository.save(account);
    }

    public Account addCreditInformation(UUID owner, UUID accountId, AddCreditInformationDto dto) {
        var account = this.getAccount(owner, accountId);

        var creditInformation = new AccountCreditInformation();
        creditInformation.setCreditLimit(dto.getCreditLimit());
        creditInformation.setCutoffDay(dto.getCutoffDay());
        creditInformation.setIntervalPaymentLimit(dto.getIntervalPaymentLimit());

        var saved = this.accountCreditInformationRepository.save(creditInformation);
        account.setCreditInformation(saved);

        return this.accountRepository.save(account);
    }

    // TODO: Should not return any movements
    public Iterable<Account> getAccounts(UUID owner) {
        return accountRepository.findAllByOwnerAndDeletedAtIsNullAndActiveIsTrue(owner);
    }

    // TODO: Should return only n number of movements. Max by default 5.
    public Account getAccount(UUID owner, UUID id) {
        var account = this.accountRepository.findByOwnerAndId(owner, id);

        if(account.isEmpty()) {
            throw new ResourceNotFound("Account not found");
        }

        return account.get();
    }

    public Account updateAccount(Account account) {
        return this.accountRepository.save(account);
    }

    public void updateTotal(Account account, BigDecimal amountChange) {
        account.setTotal(account.getTotal().add(amountChange));
        this.accountRepository.save(account);
    }
}
