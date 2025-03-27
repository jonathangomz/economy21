package com.jonathangomz.economy21.repository;

import com.jonathangomz.economy21.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends CrudRepository<Account, UUID> {
    Iterable<Account> findAllByOwnerAndDeletedAtIsNullAndActiveIsTrue(UUID owner);
    Optional<Account> findByOwnerAndId(UUID owner, UUID id);
}
