package com.jonathangomz.economy21.model.dtos;

import com.jonathangomz.economy21.model.enums.MovementType;

import java.math.BigDecimal;

public class MovementTemplate {
    public static CreateMovementDto generateInitialMovement(BigDecimal amount) {
        var movement = new CreateMovementDto();
        movement.setAmount(amount);
        movement.setType(MovementType.INITIAL);
        movement.setTitle("Default Initial Movement");
        movement.setCommerce("initial");
        return movement;
    }
}
