package com.jonathangomz.economy21.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jonathangomz.economy21.model.enums.MovementSubtype;
import com.jonathangomz.economy21.model.enums.MovementType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "movements")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MovementType type;

    @Column(nullable = false)
    private MovementSubtype subtype;

    @ManyToMany
    @JoinTable(
            name = "movements_tags",
            joinColumns = @JoinColumn(name = "movement_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    @Column(length = 100, nullable = false)
    private String commerce;

    @Column(nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate date;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100)
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean online = false;

    @Column(columnDefinition = "BOOLEAN DEFAULT NULL")
    private Boolean settled;

    @Column(columnDefinition = "INTEGER DEFAULT NULL")
    @Max(240)
    private int deferralMonths;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public BigDecimal getAmount() {
        if(this.type == MovementType.CREDIT) {
            return this.amount;
        }
        return this.amount.negate();
    }

    @Transient
    @JsonIgnore
    public boolean isCharge() {
        return this.type == MovementType.CHARGE;
    }
}