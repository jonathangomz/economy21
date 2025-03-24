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

        var savedAccount = this.accountManager.AddAccount(owner, dto);

        var initialMovementTemplate = MovementTemplate.generateInitialMovement(dto.getTotal());
        var initialMovement = this.movementManager.createMovement(savedAccount.getId(), initialMovementTemplate);

        this.accountManager.updateTotal(savedAccount, initialMovement.getAmount());

        // Not exactly the best solution but working by now
        var list = new ArrayList<Movement>();
        list.add(initialMovement);
        savedAccount.setMovements(list);

        return savedAccount;
    }

    @PostMapping("{accountId}/credit")
    public Account addCreditInformation(@PathVariable UUID accountId, @RequestBody @Valid AddCreditInformationDto dto) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        return this.accountManager.addCreditInformation(owner, accountId, dto);
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