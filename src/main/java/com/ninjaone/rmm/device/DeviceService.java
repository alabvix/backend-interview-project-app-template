package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.exception.DeviceNotFoundException;
import com.ninjaone.rmm.device.exception.ServiceAlreadyAssociatedToDeviceException;
import com.ninjaone.rmm.device.payload.*;
import com.ninjaone.rmm.service.ServiceEntity;
import com.ninjaone.rmm.service.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final ServiceRepository serviceRepository;

    private final DeviceConverter deviceConverter;

    private final MathContext mathContext = new MathContext(2);

    @Autowired
    public DeviceService(DeviceRepository deviceRepository,
                         ServiceRepository serviceRepository,
                         DeviceConverter deviceConverter) {
        this.deviceRepository = deviceRepository;
        this.serviceRepository = serviceRepository;
        this.deviceConverter = deviceConverter;
    }

    public void deleteDevice(Long deviceId) {
        deviceRepository.delete(this.getDevice(deviceId));
    }

    public DeviceOutput addDevice(AddDeviceInput input){

        DeviceEntity device = deviceConverter.toEntity(input);
        device = deviceRepository.save(device);

        if (input.servicesId.size() > 0) {
            input.servicesId.forEach(i->{
                Optional<ServiceEntity> opService = serviceRepository.findById(i);
                if (opService.isPresent()) {

                }
            });

        }

        return deviceConverter.toDeviceOutput(device);
    }

    public void associateServices(AssociateDeviceServicesInput input) {
        final DeviceEntity device = getDevice(input.deviceId);

        List<Long> filteredServicesId = input.servicesId.stream().distinct().collect(Collectors.toList());

        List<ServiceEntity> associatedServices = device.getServices().stream()
                        .filter(s -> filteredServicesId.stream()
                                .anyMatch(sid -> Objects.equals(sid, s.getId()))
                        ).collect(Collectors.toList());

        if (!associatedServices.isEmpty()) {
            final List<String> serviceNames = associatedServices.stream()
                    .map(ServiceEntity::getName).collect(Collectors.toList());
            throw new ServiceAlreadyAssociatedToDeviceException("Services already associated to device: " +
                    serviceNames);
        }

        List<ServiceEntity> services = new ArrayList<>();
        filteredServicesId.forEach(i->{
            Optional<ServiceEntity> opService = serviceRepository.findById(i);
            if (opService.isPresent()){
                services.add(opService.get());
            } else {
                throw new RuntimeException("Service not found with id: " + i);
            }
        });

        device.setServices(services);
        deviceRepository.save(device);

    }

    public void updateDevice(UpdateDeviceInput input) {

        final DeviceEntity device = getDevice(input.id);

        device.setSystemName(input.systemName);
        device.setDeviceType(input.deviceType);

        deviceRepository.save(device);
    }

    public DeviceOutput getDeviceById(Long deviceId){
        return deviceConverter.toDeviceOutput(this.getDevice(deviceId));
    }

    public CalculateOutput calculateCost(CalculateInput payload) {

        BigDecimal totalCustomer = BigDecimal.ZERO;

        for (CalculateInputDevice deviceInput: payload.getDevices()) {

            final DeviceEntity device = getDevice(deviceInput.deviceId);

            BigDecimal totalCost = device.getServices().stream()
                    .map(s -> s.getCost().multiply(BigDecimal.valueOf(deviceInput.quantity)))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalCustomer = totalCustomer.add(totalCost, mathContext);
        }

        return new CalculateOutput(totalCustomer);
    }

    public CalculateDetailedOutput calculateCostDetailed(CalculateInput payload){

        BigDecimal totalCustomer = BigDecimal.ZERO;
        final List<CalculateDetailedDeviceOutput> devices = new ArrayList<>();

        for (CalculateInputDevice deviceInput: payload.getDevices()) {

            final DeviceEntity device = getDevice(deviceInput.deviceId);
            final List<CalculateDetailedServiceOutput> services = new ArrayList<>();

            final BigDecimal totalCost = calculateServicesTotalCost(deviceInput.quantity, device, services);

            devices.add(new CalculateDetailedDeviceOutput(device.getId(), device.getSystemName(), totalCost, services));

            totalCustomer = totalCustomer.add(totalCost, mathContext);
        }

        return new CalculateDetailedOutput(totalCustomer, devices);

    }

    private DeviceEntity getDevice(Long deviceId) {
        final Optional<DeviceEntity> opDevice = deviceRepository.findById(deviceId);
        if (opDevice.isEmpty()){
            throw new DeviceNotFoundException("Device with id " + deviceId + " not found");
        }
        return opDevice.get();
    }

    private BigDecimal calculateServicesTotalCost(int quantity, DeviceEntity device, List<CalculateDetailedServiceOutput> services) {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (ServiceEntity service: device.getServices()) {
            final BigDecimal serviceCost = service.getCost().multiply(BigDecimal.valueOf(quantity));
            totalCost = totalCost.add(serviceCost);
            services.add(new CalculateDetailedServiceOutput(service.getId(), service.getName(), serviceCost));
        }
        return totalCost;
    }


}

