package com.ninjaone.rmm.service;

import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.ServiceOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    private final ServiceConverter serviceConverter;

    @Autowired
    public ServiceService(
            ServiceRepository serviceRepository,
            ServiceConverter serviceConverter) {
        this.serviceRepository = serviceRepository;
        this.serviceConverter = serviceConverter;
    }

    public ServiceOutput addService(AddServiceInput input) {
        final ServiceEntity service = serviceConverter.toEntity(input);
        return serviceConverter.toServiceOutput(serviceRepository.save(service));
    }

    public void deleteService(Long deviceId) {

    }

}
