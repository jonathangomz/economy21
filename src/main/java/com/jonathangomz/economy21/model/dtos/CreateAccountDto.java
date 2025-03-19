package com.jonathangomz.economy21.model.dtos;

import com.jonathangomz.economy21.model.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// TODO: Add jakarta validations
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
    @NotBlank(message = "{validation.account.name.notBlank}")
    @Size(max = 100, message = "{validation.account.name.size}")
    private String name;

    @NotNull(message = "Total is required")
    private BigDecimal total;

    @NotNull(message = "Type is required")
    private AccountType type;
}
