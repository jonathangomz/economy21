package com.jonathangomz.economy21.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "accounts_credit_information")
public class AccountCreditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal creditLimit;

    @Column(nullable = false)
    @Max(31)
    private int cutoffDay;

    @Column(precision = 10, scale = 2, nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal currentMonthPayment = BigDecimal.ZERO;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 20")
    private int intervalPaymentLimit = 20;

    private LocalDate nextCutoffDate;
    private LocalDate nextPaymentLimitDate;

    public LocalDate getNextCutoffDate() {
        var currentMonthCutoffDate = LocalDate.now().withDayOfMonth(this.cutoffDay);
        if(currentMonthCutoffDate.isBefore(LocalDate.now())) {
            return currentMonthCutoffDate.plusMonths(1);
        }
        return currentMonthCutoffDate;
    }

    public LocalDate getNextPaymentLimitDate() {
        var currentMonthCutoffDate = LocalDate.now().withDayOfMonth(this.cutoffDay);
        var currentMonthPaymentLimitDate = currentMonthCutoffDate.plusDays(this.intervalPaymentLimit);
        if(currentMonthPaymentLimitDate.isBefore(LocalDate.now())) {
            return currentMonthCutoffDate.plusMonths(1).plusDays(this.intervalPaymentLimit);
        }
        return currentMonthPaymentLimitDate;
    }
}