package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.exceptions.InvalidField;
import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.AccountCreditInformation;
import com.jonathangomz.economy21.model.dtos.CreateAccountDto;
import com.jonathangomz.economy21.model.dtos.UpdateAccountDto;
import com.jonathangomz.economy21.model.enums.AccountType;
import com.jonathangomz.economy21.service.AccountManager;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountManager accountManager;

    public AccountController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @GetMapping()
    public Iterable<Account> getAccounts() {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        return this.accountManager.getAccounts(owner);
    }

    @GetMapping("{accountId}")
    public Account getAccount(@PathVariable UUID accountId) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        return this.accountManager.getAccount(owner, accountId);
    }

    @PostMapping()
    public Account createAccount(@RequestBody @Valid CreateAccountDto dto) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        if(dto.getType() == AccountType.CREDIT) {
            if (dto.getCutoffDay() == null) {
                throw new InvalidField("Cutoff day is required for credit accounts");
            }
        }
        
        var savedAccount = this.accountManager.createAccount(owner, dto);

        if(dto.getType() == AccountType.CREDIT) {
            var creditInformation = new AccountCreditInformation();
            creditInformation.setCreditLimit(dto.getTotal());
            creditInformation.setCutoffDay(dto.getCutoffDay());
            creditInformation.setIntervalPaymentLimit(dto.getIntervalPaymentLimit());
            this.accountManager.addCreditInformation(owner, savedAccount.getId(), creditInformation);
        }

        return savedAccount;
    }
    
    @PutMapping("{accountId}")
    public Account updateAccount(@PathVariable UUID accountId, @RequestBody @Valid UpdateAccountDto dto) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        var account = this.accountManager.getAccount(owner, accountId);
        account.setName(dto.getName());
        return this.accountManager.updateAccount(account);
    }

    @DeleteMapping("{accountId}")
    public void deleteAccount(@PathVariable UUID accountId) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");
        var account = this.accountManager.getAccount(owner, accountId);
        account.setDeletedAt(LocalDateTime.now());
        this.accountManager.updateAccount(account);
    }

    @PatchMapping("{accountId}/status")
    public Account updateAccountStatus(@PathVariable UUID accountId, boolean active) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        var account = this.accountManager.getAccount(owner, accountId);
        account.setActive(active);
        return this.accountManager.updateAccount(account);
    }
}