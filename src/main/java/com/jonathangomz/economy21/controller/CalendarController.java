package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.model.CalendarEvent;
import com.jonathangomz.economy21.service.CalendarManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("calendar")
public class CalendarController {
    private final CalendarManager calendarManager;

    public CalendarController(CalendarManager calendarManager) {
        this.calendarManager = calendarManager;
    }

    @GetMapping
    public List<CalendarEvent> getCalendarEvents() {
        var today = LocalDate.now();
        return calendarManager.getMonthServices(today);
    }
}
