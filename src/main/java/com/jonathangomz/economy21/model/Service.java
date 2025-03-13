package com.jonathangomz.economy21.model;

import com.jonathangomz.economy21.model.enums.PaymentMethod;
import com.jonathangomz.economy21.model.enums.RecurrenceType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column()
    private String notes;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, columnDefinition = "date default CURRENT_DATE")
    private LocalDate startDate = LocalDate.now();

    @Column(nullable = false)
    private LocalDate currentPaymentDate;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'MONTH'")
    @Enumerated(EnumType.STRING)
    private RecurrenceType recurrenceType = RecurrenceType.MONTH;

    @Column(nullable = false, columnDefinition = "integer default 1")
    private Integer interval = 1;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'RECURRING'")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.RECURRING;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @Column(nullable = false)
    private UUID accountId;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column()
    private LocalDateTime deletedAt;

    @PrePersist
    private void setDefaults() {
        if (currentPaymentDate == null) {
            currentPaymentDate = startDate;
        }
    }

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