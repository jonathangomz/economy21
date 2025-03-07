package com.jonathangomz.economy21.model.dtos;

import com.jonathangomz.economy21.model.enums.PaymentMethod;
import com.jonathangomz.economy21.model.enums.RecurrenceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CreateServiceDto {
    public String name;
    public String notes;
    public BigDecimal price;
    public LocalDate startDate;
    public RecurrenceType recurrenceType;
    public Integer interval;
    public PaymentMethod paymentMethod;
    public UUID accountId;
}
