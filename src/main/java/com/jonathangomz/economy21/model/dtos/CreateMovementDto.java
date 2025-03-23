package com.jonathangomz.economy21.model.dtos;

import com.jonathangomz.economy21.model.enums.MovementSubtype;
import com.jonathangomz.economy21.model.enums.MovementType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMovementDto {
    // TODO: Add enum validations
    @NotNull(message = "Movement type is required")
    private MovementType type;

    // TODO: Add enum validations
    @NotNull(message = "Movement subtype is required")
    private MovementSubtype subtype;

    // TODO: Add list of tags comma separated

    @NotBlank(message = "{validation.commerce.notBlank}")
    @Size(max = 100, message = "Commerce name must not exceed 100 characters")
    private String commerce;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    // TODO: Do not allow add a date greater than today
    private LocalDate date = LocalDate.now();

    @NotBlank(message = "Title is required")
    @Size(max = 50, message = "Title must not exceed 50 characters")
    private String title;

    private String description = null;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount must be greater than or equal to zero")
    private BigDecimal amount;

    private boolean online = false;
}
