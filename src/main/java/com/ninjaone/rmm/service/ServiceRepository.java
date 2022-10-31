package com.ninjaone.rmm.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends CrudRepository<ServiceEntity, Long> {

    Optional<ServiceEntity> findByNameConcat(String name);
}
