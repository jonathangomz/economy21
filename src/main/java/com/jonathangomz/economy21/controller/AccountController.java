package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.dtos.CreateAccountDto;
import com.jonathangomz.economy21.model.dtos.MovementTemplate;
import com.jonathangomz.economy21.service.AccountManager;
import com.jonathangomz.economy21.service.MovementManager;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountManager accountManager;
    private final MovementManager movementManager;

    public AccountController(AccountManager accountService, MovementManager movementManager) {
        this.accountManager = accountService;
        this.movementManager = movementManager;
    }

    @GetMapping()
    public Iterable<Account> getAccounts() {
        return this.accountManager.getAccounts();
    }

    @GetMapping("{accountId}")
    public Account getAccount(@PathVariable UUID accountId) {
        return this.accountManager.getAccount(accountId);
    }

    @PostMapping()
    public Account createAccount(@RequestBody @Valid CreateAccountDto dto) {
        var owner = "jonathan";
        var newAccount = this.accountManager.AddAccount(dto, owner);

        var initialMovementTemplate = MovementTemplate.generateInitialMovement(dto.getTotal());
        this.movementManager.createMovement(newAccount.getId(), initialMovementTemplate);

        // TODO: Missing initial movement on returned JSON. But total is being changed correctly.
        return this.accountManager.getAccount(newAccount.getId());
    }
}