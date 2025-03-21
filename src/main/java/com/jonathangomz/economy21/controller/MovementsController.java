package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.model.Movement;
import com.jonathangomz.economy21.model.dtos.CreateMovementDto;
import com.jonathangomz.economy21.service.MovementManager;
import jakarta.validation.Valid;
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
    public Iterable<Movement> getMovements(@PathVariable UUID accountId) {
        return this.movementManager.getMovements(accountId);
    }

    @GetMapping("{movementId}")
    public Movement getMovements(@PathVariable UUID accountId, @PathVariable long movementId) {
        return this.movementManager.getMovement(accountId, movementId);
    }

    @PostMapping
    public Movement addMovement(@PathVariable UUID accountId, @RequestBody @Valid CreateMovementDto dto) {
        return this.movementManager.createMovement(accountId, dto);
    }

    @DeleteMapping("{movementId}")
    public void deleteMovement(@PathVariable UUID accountId, @PathVariable Long movementId) {
        this.movementManager.deleteMovement(accountId, movementId);
    }
}
