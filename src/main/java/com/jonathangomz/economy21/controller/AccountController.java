package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.AccountCreditInformation;
import com.jonathangomz.economy21.model.Movement;
import com.jonathangomz.economy21.model.dtos.AddCreditInformationDto;
import com.jonathangomz.economy21.model.dtos.CreateAccountDto;
import com.jonathangomz.economy21.model.dtos.MovementTemplate;
import com.jonathangomz.economy21.service.AccountManager;
import com.jonathangomz.economy21.service.MovementManager;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        var savedMovement = this.movementManager.createMovement(newAccount.getId(), initialMovementTemplate);

        // Not exactly the best solution but working by now
        var list = new ArrayList<Movement>();
        list.add(savedMovement);
        newAccount.setMovements(list);

        return newAccount;
    }

    @PostMapping("{accountId}/credit")
    public Account addCreditInformation(@PathVariable UUID accountId, @RequestBody @Valid AddCreditInformationDto dto) {
        return this.accountManager.addCreditInformation(accountId, dto);
    }

    @GetMapping("{accountId}/credit")
    public AccountCreditInformation getCreditInformation(@PathVariable UUID accountId) {
        var account = this.accountManager.getAccount(accountId);
        if(account.getCreditInformation() == null) {
            throw new ResourceNotFound("Credit information not found");
        }
        return account.getCreditInformation();
    }
}