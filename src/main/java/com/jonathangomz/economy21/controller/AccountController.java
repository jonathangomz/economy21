package com.jonathangomz.economy21.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {

    @GetMapping()
    public String sayHello() {
        return "Hello, welcome to the Economy21 application!";
    }
}