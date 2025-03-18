package com.jonathangomz.economy21.model.dtos;

import com.jonathangomz.economy21.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// TODO: Add jakarta validations
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
    private String name;
    private BigDecimal total;
    private AccountType type;
}
