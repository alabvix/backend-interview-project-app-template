package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.exception.DeviceNotFoundException;
import com.ninjaone.rmm.device.payload.*;
import com.ninjaone.rmm.service.ServiceEntity;
import com.ninjaone.rmm.service.ServiceNotFoundException;
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

    public GetDeviceOutput getDeviceById(Long deviceId){
        return deviceConverter.toDeviceOutput(this.getDevice(deviceId));
    }

    public AddDeviceOutput addDevice(AddDeviceInput input){
        final DeviceEntity device = deviceConverter.toEntity(input);
        return deviceConverter.toAddDeviceOutput(deviceRepository.save(device));
    }

    public void deleteDevice(Long deviceId) {
        DeviceEntity device = this.getDevice(deviceId);

        for (ServiceEntity service: device.getServices()){
            service.removeDevice(device);
        }

        deviceRepository.delete(device);
    }

    public void associateServices(AssociateDeviceServicesInput input) {
        final DeviceEntity device = getDevice(input.deviceId);

        final List<Long> filteredServicesId = input.servicesId.stream().distinct().collect(Collectors.toList());

        for (Long id: filteredServicesId){
            final long total = device.getServices().stream().filter(s->Objects.equals(s.getId(), id)).count();
            if (total > 0) {
                continue;
            }

            Optional<ServiceEntity> opService = serviceRepository.findById(id);
            if (opService.isPresent()){
                device.addService(opService.get());
            } else {
                throw new ServiceNotFoundException("Service not found with id: " + id);
            }
        }

        deviceRepository.save(device);
    }

    public void updateDevice(UpdateDeviceInput input) {
        final DeviceEntity device = getDevice(input.id);

        device.setSystemName(input.systemName);
        device.setDeviceType(input.deviceType);

        deviceRepository.save(device);
    }

    public CalculateOutput calculateCost(CalculateInput input) {

        BigDecimal totalCustomer = BigDecimal.ZERO;

        for (CalculateInputDevice deviceInput: input.getDevices()) {

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
            totalCost = totalCost.add(serviceCost, mathContext);
            services.add(new CalculateDetailedServiceOutput(service.getId(), service.getName(), serviceCost));
        }
        return totalCost;
    }

}

