package com.ninjaone.rmm.service;

import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.AddServiceOutput;
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

    public AddServiceOutput addService(AddServiceInput input) {
        final ServiceEntity service = serviceConverter.toEntity(input);
        return serviceConverter.toAddServiceOutput(serviceRepository.save(service));
    }

    public void deleteService(Long serviceId) {
        this.serviceRepository.delete(getService(serviceId));
    }

    public GetServiceOutput getServiceById(Long serviceId) {
        return serviceConverter.toGetServiceOutput(getService(serviceId));
    }

    private ServiceEntity getService(Long serviceId) {
        final Optional< ServiceEntity> opService = serviceRepository.findById(serviceId);
        if (opService.isEmpty()){
            throw new ServiceNotFoundException("Service with id " + serviceId + " not found");
        }
        return opService.get();
    }

}
