package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.Dtos.CreateAccountDto;
import com.jonathangomz.economy21.model.Dtos.UpdateAccountDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AccountService {
    private final ArrayList<Account> accounts;

    public AccountService() {
        accounts = new ArrayList<>();
    }

    public Account AddAccount(CreateAccountDto dto, String userId) {
        long newId = GetNextId();

        var account = new Account();
        account.id = newId;
        account.name = dto.name;
        account.movements = new ArrayList<>();
        account.userId = userId;

        accounts.add(account);

        return account;
    }

    public ArrayList<Account> GetAccounts() {
        return accounts;
    }

    public Account GetAccount(long id) {
        return accounts.stream().filter(a -> a.id == id).findFirst().orElse(null);
    }

    public boolean RemoveAccount(long id) {
        var account = this.GetAccount(id);
        if(account != null) {
            accounts.remove(account);
            return true;
        }
        return false;
    }

    public void UpdateAccount(long accountId, UpdateAccountDto dto) {
        var account = this.GetAccount(accountId);
        if (account != null) {
            account.name = dto.name;
            accounts.remove(account);
            accounts.add(account);
        }
    }

    private long GetNextId() {
        return accounts.stream().mapToLong(a -> a.id).max().orElse(0) + 1;
    }
}
