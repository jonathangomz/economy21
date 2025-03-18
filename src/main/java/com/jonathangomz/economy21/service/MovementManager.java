package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Movement;
import com.jonathangomz.economy21.model.dtos.CreateMovementDto;
import com.jonathangomz.economy21.repository.MovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovementManager {
    private final AccountManager accountManager;
    private final MovementRepository movementRepository;

    MovementManager(AccountManager accountManager, MovementRepository movementRepository) {
        this.accountManager = accountManager;
        this.movementRepository = movementRepository;
    }

    public Iterable<Movement> getMovements(UUID accountId) {
        var movements = this.movementRepository.findAllByAccountIdAndDeletedAtIsNull(accountId);
        if(movements == null) {
            return List.of();
        }
        return movements;
    }

    public Movement getMovement(Long id) {
        var movement = this.movementRepository.findById(id);
        if(movement.isEmpty()) {
            throw new ResourceNotFound("Movement not found");
        }
        return movement.get();
    }

    public Movement createMovement(UUID accountId, CreateMovementDto dto) {
        // Search the account
        var account = this.accountManager.getAccount(accountId);

        // Map the movement from the dto
        var movement = new Movement();
        movement.setType(dto.getType());
        movement.setCommerce(dto.getCommerce());
        movement.setTitle(dto.getTitle());
        movement.setDescription(dto.getDescription());
        movement.setAmount(dto.getAmount());
        movement.setOnline(dto.isOnline());
        movement.setAccountId(account.getId());

        // Save the movement
        var savedMovement = this.movementRepository.save(movement);

        // Update account total
        // TODO: Improve using MovementListener
        account.setTotal(account.getTotal().add(movement.getAmount()));
        this.accountManager.updateAccount(account.getId(), account);

        // Return the saved movement
        return savedMovement;
    }
}
