package com.jonathangomz.economy21.repository;

import com.jonathangomz.economy21.model.Service;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
public interface CrudRepository<T, ID> extends Repository<T, ID> {

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    Iterable<T> findAll();

    @Query("SELECT s FROM Service s WHERE s.deletedAt IS NULL")
    Iterable<Service> findAllActive();

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);
}