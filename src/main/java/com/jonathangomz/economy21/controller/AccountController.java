package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.dtos.CreateAccountDto;
import com.jonathangomz.economy21.service.AccountManager;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountManager accountManager;

    public AccountController(AccountManager accountService) {
        this.accountManager = accountService;
    }

    @GetMapping()
    public Iterable<Account> GetAccounts() {
        return this.accountManager.getAccounts();
    }

    @GetMapping("{accountId}")
    public Account GetAccount(@PathVariable UUID accountId) {
        return this.accountManager.getAccount(accountId);
    }

    @PostMapping()
    public Account CreateAccount(@RequestBody CreateAccountDto dto) {
        var owner = "jonathan";
        return this.accountManager.AddAccount(dto, owner);
    }
}