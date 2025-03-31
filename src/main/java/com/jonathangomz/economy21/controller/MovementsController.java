package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.model.Movement;
import com.jonathangomz.economy21.model.dtos.CreateMovementDto;
import com.jonathangomz.economy21.model.enums.AccountType;
import com.jonathangomz.economy21.service.AccountManager;
import com.jonathangomz.economy21.service.MovementManager;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts/{accountId}/movements")
public class MovementsController {

    private final AccountManager accountManager;
    private final MovementManager movementManager;

    public MovementsController(AccountManager accountManager, MovementManager movementManager) {
        this.accountManager = accountManager;
        this.movementManager = movementManager;
    }

    @GetMapping
    public Iterable<Movement> getMovements(@PathVariable UUID accountId) {
        return this.movementManager.getMovements(accountId);
    }

    @GetMapping("{movementId}")
    public Movement getMovement(@PathVariable UUID accountId, @PathVariable long movementId) {
        return this.movementManager.getMovement(accountId, movementId);
    }

    @PostMapping
    public Movement addMovement(@PathVariable UUID accountId, @RequestBody @Valid CreateMovementDto dto) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        var account = this.accountManager.getAccount(owner, accountId);

        // TODO: Cannot create movement is do not have enough money on the account
        var createdMovement = this.movementManager.createMovement(accountId, dto, account.isDebit());

        this.accountManager.updateTotal(account, createdMovement.getAmount());

        if(account.getType() == AccountType.CREDIT) {
            this.accountManager.recalculateMonthlyDebt(account, createdMovement.getAmount());
        }

        return createdMovement;
    }

    @DeleteMapping("{movementId}")
    public void deleteMovement(@PathVariable UUID accountId, @PathVariable Long movementId) {
        // TODO: Replace with user id from context
        var owner = UUID.fromString("e7dc9147-7c56-4a41-912d-8c8e9ef3a1e8");

        var account = this.accountManager.getAccount(owner, accountId);

        var deletedMovement = this.movementManager.deleteMovement(accountId, movementId);

        this.accountManager.updateTotal(account, deletedMovement.getAmount().negate());
    }
}
