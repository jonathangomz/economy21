package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.model.CalendarEvent;

import java.time.LocalDate;
import java.util.List;

@org.springframework.stereotype.Service
public class CalendarManager {
    private final ServiceManager serviceManager;

    public CalendarManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public List<CalendarEvent> getMonthServices(LocalDate date) {
        return serviceManager.getServices()
                .stream()
                .filter(s -> s.getCurrentPaymentDate()
                        .getMonth()
                        .equals(date.getMonth()))
                .map(s -> {
                    var event = new CalendarEvent();
                    event.setName(s.getName());
                    event.setAmount(s.getPrice().negate());
                    event.setDate(s.getCurrentPaymentDate());
                    return event;
                })
                .toList();
    }
}
