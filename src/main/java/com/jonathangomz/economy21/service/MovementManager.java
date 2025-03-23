package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.Movement;
import com.jonathangomz.economy21.model.dtos.CreateMovementDto;
import com.jonathangomz.economy21.repository.MovementRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class MovementManager {
    private final AccountManager accountManager;
    private final MovementRepository movementRepository;

    MovementManager(AccountManager accountManager, MovementRepository movementRepository) {
        this.accountManager = accountManager;
        this.movementRepository = movementRepository;
    }

    public Iterable<Movement> getMovements(UUID accountId) {
        var movements = this.movementRepository.findAllByAccountId(accountId);
        if(movements == null) {
            return List.of();
        }

        return StreamSupport.stream(movements.spliterator(), false)
                .sorted((m1, m2) -> m2.getDate().compareTo(m1.getDate()))
                .toList();
    }

    public Movement getMovement(UUID accountId, Long id) {
        var movement = this.movementRepository.findByAccountIdAndId(accountId, id);
        if(movement.isEmpty()) {
            throw new ResourceNotFound("Movement not found");
        }
        return movement.get();
    }

    public Movement createMovement(UUID accountId, CreateMovementDto dto) {
        // Search the account
        var account = this.accountManager.getAccount(accountId);

        // TODO: tags are being null
        // Map the movement from the dto
        var movement = new Movement();
        movement.setType(dto.getType());
        movement.setSubtype(dto.getSubtype());
        movement.setCommerce(dto.getCommerce());
        movement.setDate(dto.getDate());
        movement.setTitle(dto.getTitle());
        movement.setDescription(dto.getDescription());
        movement.setAmount(dto.getAmount());
        movement.setOnline(dto.isOnline());
        movement.setAccountId(account.getId());

        // Save the movement
        var savedMovement = this.movementRepository.save(movement);

        // Update account total
        this.updateAccountTotal(account, movement.getAmount());

        // Return the saved movement
        return savedMovement;
    }

    public void deleteMovement(UUID accountId, Long id) {
        var movement = this.getMovement(accountId, id);
        this.movementRepository.deleteById(movement.getId());

        // Update account total
        var account = this.accountManager.getAccount(accountId);
        updateAccountTotal(account, movement.getAmount().negate());
    }

    private void updateAccountTotal(Account account, BigDecimal amountChange) {
        account.setTotal(account.getTotal().add(amountChange));
        this.accountManager.updateAccount(account.getId(), account);
    }
}
