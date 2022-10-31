package com.ninjaone.rmm.service;

import com.ninjaone.rmm.device.DeviceEntity;
import com.ninjaone.rmm.device.exception.DeviceNotFoundException;
import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.GetServiceOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public GetServiceOutput addService(AddServiceInput input) {
        final ServiceEntity service = serviceConverter.toEntity(input);
        return serviceConverter.toGetServiceOutput(serviceRepository.save(service));
    }

    public void deleteService(Long deviceId) {

    }

    public GetServiceOutput getService(Long serviceId) {
        final Optional<ServiceEntity> opService = serviceRepository.findById(serviceId);
        if (opService.isEmpty()){
            throw new DeviceNotFoundException("Service with id " + serviceId + " not found");
        }
        return serviceConverter.toGetServiceOutput(opService.get());
    }

}
