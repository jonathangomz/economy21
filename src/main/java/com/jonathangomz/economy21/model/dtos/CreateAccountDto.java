package com.jonathangomz.economy21.model.dtos;

import com.jonathangomz.economy21.model.enums.AccountType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto {
    @NotBlank(message = "{validation.account.name.notBlank}")
    @Size(max = 100, message = "{validation.account.name.size}")
    private String name;

    @NotNull(message = "Type is required")
    private AccountType type;

    @NotNull(message = "Total is required")
    @Min(value = 0, message = "Total must be greater than or equal to zero")
    private BigDecimal total;

    @Min(value = 1, message = "{validation.cutoffDay.min}")
    @Max(value = 31, message = "{validation.cutoffDay.max}")
    private Integer cutoffDay;

    @Min(value = 1, message = "{validation.intervalPaymentLimit.min}")
    @Max(value = 30, message = "{validation.intervalPaymentLimit.max}")
    private Integer intervalPaymentLimit;

    public int getIntervalPaymentLimit() {
        return this.intervalPaymentLimit == null ? 20 : this.intervalPaymentLimit;
    }
}
