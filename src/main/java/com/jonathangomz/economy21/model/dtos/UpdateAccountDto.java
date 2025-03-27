package com.jonathangomz.economy21.model.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountDto {
    @NotBlank(message = "{validation.account.name.notBlank}")
    @Size(max = 100, message = "{validation.account.name.size}")
    protected String name;
}
