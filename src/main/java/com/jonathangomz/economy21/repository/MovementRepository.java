package com.jonathangomz.economy21.repository;

import com.jonathangomz.economy21.model.Movement;

import java.util.Optional;
import java.util.UUID;

public interface MovementRepository extends CrudRepository<Movement, Long> {
    Iterable<Movement> findAllByAccountId(UUID accountId);
    Optional<Movement> findByAccountIdAndId(UUID accountId, Long id);
}
