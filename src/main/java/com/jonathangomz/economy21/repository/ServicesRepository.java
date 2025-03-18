package com.jonathangomz.economy21.repository;

import com.jonathangomz.economy21.model.Service;
import org.springframework.data.jpa.repository.Query;

public interface ServicesRepository extends CrudRepository<Service, Long> {
    @Query("SELECT s FROM services s WHERE s.deletedAt IS NULL")
    Iterable<Service> findAllActive();
}
