package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.AccountCreditInformation;
import com.jonathangomz.economy21.model.dtos.CreateAccountDto;
import com.jonathangomz.economy21.model.mappers.AccountMapper;
import com.jonathangomz.economy21.repository.AccountCreditInformationRepository;
import com.jonathangomz.economy21.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountManager {
    private final AccountRepository accountRepository;
    private final AccountCreditInformationRepository accountCreditInformationRepository;
    private final AccountMapper accountMapper;

    public AccountManager(AccountRepository accountRepository, AccountCreditInformationRepository accountCreditInformationRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountCreditInformationRepository = accountCreditInformationRepository;
        this.accountMapper = accountMapper;
    }

    public Account createAccount(UUID owner, CreateAccountDto dto) {
        var account = accountMapper.createDtoToEntity(dto);
        account.setOwner(owner);
        return this.accountRepository.save(account);
    }

    public void addCreditInformation(UUID owner, UUID accountId, AccountCreditInformation dto) {
        var account = this.getAccount(owner, accountId);

        var creditInformation = new AccountCreditInformation();
        creditInformation.setCreditLimit(dto.getCreditLimit());
        creditInformation.setCutoffDay(dto.getCutoffDay());
        creditInformation.setIntervalPaymentLimit(dto.getIntervalPaymentLimit());

        var saved = this.accountCreditInformationRepository.save(creditInformation);
        account.setCreditInformation(saved);

        this.accountRepository.save(account);
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

    public void recalculateMonthlyDebt(Account account, BigDecimal amount) {
        // TODO: get all movements from the current period and recalculate the total
        var creditInformation = account.getCreditInformation();
        var newTotal = creditInformation.getCurrentMonthPayment().add(amount);
        creditInformation.setCurrentMonthPayment(newTotal);
        this.accountCreditInformationRepository.save(creditInformation);
    }
}
