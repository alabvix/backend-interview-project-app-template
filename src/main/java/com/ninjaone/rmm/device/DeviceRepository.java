package com.ninjaone.rmm.device;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends CrudRepository<DeviceEntity, Long> {

    Optional<DeviceEntity> findBySystemNameConcat(String systemName);
}
