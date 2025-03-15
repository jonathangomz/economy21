package com.jonathangomz.economy21.service;

import com.jonathangomz.economy21.exceptions.ResourceNotFound;
import com.jonathangomz.economy21.model.dtos.CreateServiceDto;
import com.jonathangomz.economy21.model.Service;
import com.jonathangomz.economy21.repository.ServicesRepository;

import java.util.ArrayList;

@org.springframework.stereotype.Service
public class ServiceManager {
    private final ServicesRepository servicesRepository;

    private ServiceManager(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    public Service createService(CreateServiceDto dto) {
        var service = new Service();
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

        return this.servicesRepository.save(service);
    }

    public Iterable<Service> getServices() {
        // TODO[think]: where do I recalculate the next payment date
        var services = this.servicesRepository.findAllActive();
        services.forEach(Service::calculateNextPaymentDate);
        return services;
    }

    public Service getService(long serviceId) {
        var services = new ArrayList<Service>();
        this.getServices().forEach(services::add);

        var service = services.stream().filter(s -> s.getId() == serviceId).findFirst().orElse(null);
        if (service == null) {
            throw new ResourceNotFound("Service not found");
        }
        return service;
    }

    public void deleteService(Long serviceId) {
        this.servicesRepository.deleteById(serviceId);
    }
}
