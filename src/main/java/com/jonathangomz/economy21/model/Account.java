package com.jonathangomz.economy21.model;

import com.jonathangomz.economy21.model.enums.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private AccountType type;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private List<Movement> movements;

    @Column(length = 100, nullable = false)
    private String owner;

//    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
//    private CreditAccountInformation creditAccountInformation;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(updatable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    public List<Movement> getMovements() {
        return movements == null ? List.of() : movements;
    }
}