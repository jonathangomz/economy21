package com.jonathangomz.economy21.model;

import com.jonathangomz.economy21.model.enums.PaymentMethod;
import com.jonathangomz.economy21.model.enums.RecurrenceType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    // TODO: pk autogenerated
    private Long id;

    // TODO: required
    private String name;

    // TODO: can be null
    private String notes;

    // TODO: required
    private BigDecimal price;

    // TODO: default today, cannot be null
    private LocalDate startDate;

    // TODO: default startDate. Then it keeps changing
    private LocalDate currentPaymentDate;

    // TODO: default Month, cannot be null
    private RecurrenceType recurrenceType;

    // TODO: default 1, cannot be null
    private Integer interval;

    // TODO: default recurring, cannot be null
    private PaymentMethod paymentMethod;

    // TODO: default true, cannot be null
    private boolean active;

    // TODO: required
    private UUID accountId;

    public LocalDate calculateNextPaymentDate() {
        if (currentPaymentDate.isBefore(LocalDate.now())) {
            currentPaymentDate = switch (recurrenceType) {
                case DAY -> currentPaymentDate.plusDays(interval);
                case WEEK -> currentPaymentDate.plusWeeks(interval);
                case MONTH -> currentPaymentDate.plusMonths(interval);
                case YEAR -> currentPaymentDate.plusYears(interval);
            };
            return calculateNextPaymentDate();
        }
        return switch (recurrenceType) {
            case DAY -> currentPaymentDate.plusDays(interval);
            case WEEK -> currentPaymentDate.plusWeeks(interval);
            case MONTH -> currentPaymentDate.plusMonths(interval);
            case YEAR -> currentPaymentDate.plusYears(interval);
        };
    }
}