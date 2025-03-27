package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.AccountCreditInformation;
import com.jonathangomz.economy21.model.dtos.AddCreditInformationDto;
import com.jonathangomz.economy21.service.AccountManager;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts/{accountId}/credit")
public class AccountCreditController {

    private final AccountManager accountManager;

    public AccountCreditController(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    public AccountCreditInformation getCreditInformation(@PathVariable UUID accountId) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        var account = this.accountManager.getAccount(owner, accountId);
        if(account.getCreditInformation() == null) {
            throw new ResourceNotFound("Credit information not found");
        }
        return account.getCreditInformation();
    }

    public Account addCreditInformation(@PathVariable UUID accountId, @RequestBody @Valid AddCreditInformationDto dto) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        return this.accountManager.addCreditInformation(owner, accountId, dto);
    }
}
