package com.jonathangomz.economy21.repository;

import com.jonathangomz.economy21.model.Movement;

import java.util.UUID;

public interface MovementRepository extends CrudRepository<Movement, Long> {
    Iterable<Movement> findAllByAccountIdAndDeletedAtIsNull(UUID accountId);
}
