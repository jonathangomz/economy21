package com.jonathangomz.economy21.model;

import com.jonathangomz.economy21.model.enums.AccountType;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 100, nullable = false)
    private String name;

    // TODO[linked_2]: Add 0 by default
    @Column(precision = 10, scale = 2, nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal total = BigDecimal.ZERO;

    @Column(nullable = false)
    private AccountType type;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private List<Movement> movements = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean active = true;

    @Column(length = 36, nullable = false)
    private UUID owner;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "credit_information_id", referencedColumnName = "id", unique = true, columnDefinition = "BIGINT DEFAULT NULL")
    private AccountCreditInformation creditInformation;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(updatable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    // TODO[linked_1]: Add this field to the AccountCreditInformation class
    private BigDecimal currentMonthPayment = null;

    public List<Movement> getMovements() {
        return movements == null ? List.of() : movements;
    }

    // TODO[linked_1]: Add this field to the AccountCreditInformation class
    public BigDecimal getCurrentMonthPayment() {
        // TODO: Update column on AccountCreditInformation
        if(creditInformation == null) {
            return null;
        }

        var nextCutoffDate = this.creditInformation.getNextCutoffDate();
        var previousCutoffDate = nextCutoffDate.minusMonths(1);

        return movements
                .stream()
                .filter(m ->
                        m.getDate().isAfter(previousCutoffDate) &&
                        m.getDate().isBefore(nextCutoffDate) &&
                        m.getAmount().compareTo(BigDecimal.ZERO) < 0)
                .map(Movement::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}