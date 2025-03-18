package com.jonathangomz.economy21.repository;

import com.jonathangomz.economy21.model.Account;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AccountRepository extends CrudRepository<Account, UUID> {
    @Query("SELECT a FROM accounts a WHERE a.deletedAt IS NULL")
    Iterable<Account> findAllActive();
}
