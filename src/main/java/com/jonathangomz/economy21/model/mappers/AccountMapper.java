package com.jonathangomz.economy21.model.mappers;

import com.jonathangomz.economy21.model.Account;
import com.jonathangomz.economy21.model.dtos.CreateAccountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account createDtoToEntity(CreateAccountDto dto);
}
