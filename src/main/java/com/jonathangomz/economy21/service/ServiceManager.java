package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.model.Dtos.CreateServiceDto;
import com.jonathangomz.economy21.model.Service;

import java.util.ArrayList;

@org.springframework.stereotype.Service
public class ServiceManager {
    private final ArrayList<Service> services = new ArrayList<>();

    public ArrayList<Service> getServices() {
        // TODO[think]: where do I recalculate the next payment date
        services.forEach(Service::calculateNextPaymentDate);
        return services;
    }

    public Service getService(long serviceId) {
        return services.stream().filter(s -> s.getId() == serviceId).findFirst().orElse(null);
    }

    public Service AddService(CreateServiceDto dto) {
        var service = new Service();
        service.setId(getNextId());
        service.setName(dto.name);
        service.setNotes(dto.notes);
        service.setPrice(dto.price);
        service.setStartDate(dto.startDate);
        service.setCurrentPaymentDate(dto.startDate);
        service.setRecurrenceType(dto.recurrenceType);
        service.setInterval(dto.interval);
        service.setPaymentMethod(dto.paymentMethod);
        service.setAccountId(dto.accountId);

        // Update payment date if needed
        service.calculateNextPaymentDate();

        services.add(service);
        return service;
    }

    private Long getNextId() {
        return services.stream().mapToLong(Service::getId).max().orElse(0) + 1;
    }
}
