package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.Dtos.CreateAccountDto;
import com.jonathangomz.economy21.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping()
    public ArrayList<Account> GetAccounts() {
        return accountService.GetAccounts();
    }

    @PostMapping()
    public Account CreateAccount(@RequestBody CreateAccountDto dto) {
        var userId = UUID.randomUUID().toString();
        return accountService.AddAccount(dto, userId);
    }
}