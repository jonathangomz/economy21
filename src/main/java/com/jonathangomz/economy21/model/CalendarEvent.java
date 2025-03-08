package com.jonathangomz.economy21.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CalendarEvent {
    private String name;
    private LocalDate date;
    private BigDecimal amount;
}
