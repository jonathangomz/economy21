package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.Dtos.CreateAccountDto;
import com.jonathangomz.economy21.model.Dtos.UpdateAccountDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class AccountService {
    private final ArrayList<Account> accounts;

    public AccountService() {
        accounts = new ArrayList<>();
    }

    public Account AddAccount(CreateAccountDto dto, String userId) {
        var account = new Account();
        account.setName(dto.name);
        account.setUserId(userId);

        accounts.add(account);
        return account;
    }

    public ArrayList<Account> GetAccounts() {
        return accounts;
    }

    public Account GetAccount(UUID id) {
        return accounts.stream().filter(a -> a.id.equals(id)).findFirst().orElse(null);
    }

    public boolean RemoveAccount(UUID id) {
        var account = this.GetAccount(id);
        if(account != null) {
            accounts.remove(account);
            return true;
        }
        return false;
    }

    public void UpdateAccount(UUID accountId, UpdateAccountDto dto) {
        var account = this.GetAccount(accountId);
        if (account != null) {
            account.name = dto.name;
            accounts.remove(account);
            accounts.add(account);
        }
    }
}
