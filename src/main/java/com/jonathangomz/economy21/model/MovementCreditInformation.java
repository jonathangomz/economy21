package com.jonathangomz.economy21.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "movements_credit_information")
@NoArgsConstructor
@AllArgsConstructor
public class MovementCreditInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean settled;

    @Column()
    @Max(240)
    private int months;

    @Column(name = "movement_id", nullable = false)
    private Long movementId;
}
