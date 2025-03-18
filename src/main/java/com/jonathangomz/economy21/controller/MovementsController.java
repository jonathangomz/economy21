package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.model.Movement;
import com.jonathangomz.economy21.model.dtos.CreateMovementDto;
import com.jonathangomz.economy21.service.AccountManager;
import com.jonathangomz.economy21.service.MovementManager;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts/{accountId}/movements")
public class MovementsController {

    private final MovementManager movementManager;

    public MovementsController(MovementManager movementManager) {
        this.movementManager = movementManager;
    }

    @GetMapping
    public Iterable<Movement> GetMovements(@PathVariable UUID accountId) {
        return this.movementManager.getMovements(accountId);
    }

    @PostMapping
    public Movement AddMovement(@PathVariable UUID accountId, @RequestBody CreateMovementDto dto) {
        return this.movementManager.createMovement(accountId, dto);
    }
}
