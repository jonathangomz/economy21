package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.AccountCreditInformation;
import com.jonathangomz.economy21.model.dtos.AddCreditInformationDto;
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
            if (dto.getCreditInformation() == null) {
                // TODO: throw a validation error. Credit accounts need credit information
            } else {
                dto.setTotal(dto.getCreditInformation().getCreditLimit());
            }
        }
        else {
            if(dto.getTotal() == null) {
                // TODO: throw a validation error. Debit accounts need an initial total
            }
        }
        
        var savedAccount = this.accountManager.createAccount(owner, dto);

        if(dto.getType() == AccountType.CREDIT) {
            this.accountManager.addCreditInformation(owner, savedAccount.getId(), dto.getCreditInformation());
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

    @PostMapping("{accountId}/credit")
    public Account addCreditInformation(@PathVariable UUID accountId, @RequestBody @Valid AddCreditInformationDto dto) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        return this.accountManager.addCreditInformation(owner, accountId, dto);
    }

    @PatchMapping("{accountId}/status")
    public Account updateAccountStatus(@PathVariable UUID accountId, boolean active) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        var account = this.accountManager.getAccount(owner, accountId);
        account.setActive(active);
        return this.accountManager.updateAccount(account);
    }

    @GetMapping("{accountId}/credit")
    public AccountCreditInformation getCreditInformation(@PathVariable UUID accountId) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        var account = this.accountManager.getAccount(owner, accountId);
        if(account.getCreditInformation() == null) {
            throw new ResourceNotFound("Credit information not found");
        }
        return account.getCreditInformation();
    }
}