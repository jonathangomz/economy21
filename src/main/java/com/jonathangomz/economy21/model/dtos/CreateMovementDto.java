package com.jonathangomz.economy21.model.dtos;

import com.jonathangomz.economy21.model.enums.MovementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovementDto {
    private MovementType type;
    // TODO: Add list of tags comma separated
    private String commerce;
    private String title;
    private String description = null;
    private BigDecimal amount;
    private boolean online = false;
}
