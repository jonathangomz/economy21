package com.jonathangomz.economy21.model.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;

@Data
public class AddCreditInformationDto {

    @NotNull(message = "{validation.creditLimit.required}")
    @DecimalMin(value = "0.01", message = "{validation.creditLimit.min}")
    @DecimalMax(value = "500000", message = "{validation.creditLimit.max}")
    private BigDecimal creditLimit;

    @NotNull(message = "{validation.cutoffDay.required}")
    @Min(value = 1, message = "{validation.cutoffDay.min}")
    @Max(value = 31, message = "{validation.cutoffDay.max}")
    private int cutoffDay;

    @Min(value = 1, message = "{validation.intervalPaymentLimit.min}")
    @Max(value = 30, message = "{validation.intervalPaymentLimit.max}")
    private Integer intervalPaymentLimit;

    public int getIntervalPaymentLimit() {
        return intervalPaymentLimit == null ? 20 : intervalPaymentLimit;
    }
}