package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.*;
import com.ninjaone.rmm.service.ServiceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final DeviceConverter deviceConverter;

    private final MathContext mathContext = new MathContext(2);

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, DeviceConverter deviceConverter) {
        this.deviceRepository = deviceRepository;
        this.deviceConverter = deviceConverter;
    }

    public void deleteDevice(Long deviceId) {
        deviceRepository.delete(this.getDevice(deviceId));
    }

    public DeviceOutput addDevice(AddDeviceInput payload){
        final DeviceEntity device = deviceRepository.save(
                deviceConverter.toEntity(payload)
        );

        return deviceConverter.toDeviceOutput(device);
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

