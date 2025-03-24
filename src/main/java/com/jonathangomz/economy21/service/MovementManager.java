package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.Movement;
import com.jonathangomz.economy21.model.dtos.CreateMovementDto;
import com.jonathangomz.economy21.repository.MovementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class MovementManager {
    private final MovementRepository movementRepository;

    MovementManager(MovementRepository movementRepository) {
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
        movement.setAccountId(accountId);

        return this.movementRepository.save(movement);
    }

    public Movement deleteMovement(UUID accountId, Long id) {
        var movement = this.getMovement(accountId, id);
        this.movementRepository.deleteById(movement.getId());
        return movement;
    }
}
