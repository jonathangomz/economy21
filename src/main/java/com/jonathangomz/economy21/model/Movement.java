package com.jonathangomz.economy21.model;

import com.jonathangomz.economy21.model.enums.MovementType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "movements")
@SQLDelete(sql = "UPDATE movements SET deleted_at = NOW() WHERE id = ?")
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MovementType type;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "movement_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @Column(length = 100, nullable = false)
    private String commerce;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100)
    private String description;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean online = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;
}