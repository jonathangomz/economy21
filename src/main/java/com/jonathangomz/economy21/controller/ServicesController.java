package com.jonathangomz.economy21.controller;

import com.jonathangomz.economy21.model.dtos.CreateServiceDto;
import com.jonathangomz.economy21.model.Service;
import com.jonathangomz.economy21.service.ServiceManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
public class ServicesController {
    private final ServiceManager serviceManager;

    public ServicesController(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    @GetMapping()
    public Iterable<Service> getServices() {
        return serviceManager.getServices();
    }

    @GetMapping("{serviceId}")
    public Service getService(@PathVariable("serviceId") Long id) {
        return serviceManager.getService(id);
    }

    @PostMapping()
    public Service createService(@RequestBody CreateServiceDto createServiceDto) {
        return serviceManager.createService(createServiceDto);
    }

    @DeleteMapping("{serviceId}")
    public void deleteService(@PathVariable("serviceId") Long id) {
        this.serviceManager.deleteService(id);
    }
}
