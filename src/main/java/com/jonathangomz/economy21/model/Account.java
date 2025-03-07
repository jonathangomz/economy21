package com.jonathangomz.economy21.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    public UUID id = UUID.randomUUID();
    public String name;
    public ArrayList<Movement> movements = new ArrayList<>();
    public String userId;
}
