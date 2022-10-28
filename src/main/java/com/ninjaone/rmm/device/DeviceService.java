package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.rest.*;
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

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, DeviceConverter deviceConverter) {
        this.deviceRepository = deviceRepository;
        this.deviceConverter = deviceConverter;
    }

    public DeviceResponse saveDevice(AddDeviceInput payload){
        final DeviceEntity device = deviceRepository.save(
                deviceConverter.toEntity(payload)
        );

        return deviceConverter.toDeviceResponse(device);
    }

    public DeviceResponse getDevice(Long deviceId){

        Optional<DeviceEntity> optional = deviceRepository.findById(deviceId);
        if (optional.isEmpty()){
            throw new DeviceNotFoundException("Device with id " + deviceId + " not found");
        }

        return deviceConverter.toDeviceResponse(optional.get());
    }

    public CalculateOutput calculateDetailedCost(CalculateInput payload){

        BigDecimal totalCustomer = BigDecimal.ZERO;
        final MathContext mathContext = new MathContext(2);
        final List<DeviceOutput> devices = new ArrayList<>();

        for (DeviceInput deviceInput: payload.getDevices()) {

            final Optional<DeviceEntity> opDevice = deviceRepository.findById(deviceInput.deviceId);
            if (opDevice.isEmpty()){
                throw new DeviceNotFoundException("Device with id " + deviceInput.deviceId + " not found");
            }

//            BigDecimal totalCost = opDevice.get().getServices().stream()
//                    .map(s -> s.getCost().multiply(BigDecimal.valueOf(deviceInput.quantity)))
//                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            final List<DeviceServiceOutput> services = new ArrayList<>();

            BigDecimal totalCost = calculateServicesTotalCost(deviceInput.quantity, opDevice.get(), services);

            devices.add(new DeviceOutput(
                    opDevice.get().getId(), opDevice.get().getSystemName(), totalCost, services));

            totalCustomer = totalCustomer.add(totalCost, mathContext);
        }

        return new CalculateOutput(totalCustomer, devices);

    }

    private BigDecimal calculateServicesTotalCost(int quantity, DeviceEntity device, List<DeviceServiceOutput> services) {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (ServiceEntity service: device.getServices()) {
            final BigDecimal serviceCost = service.getCost().multiply(BigDecimal.valueOf(quantity));
            totalCost = totalCost.add(serviceCost);
            services.add(new DeviceServiceOutput(service.getId(), service.getName(), serviceCost));
        }
        return totalCost;
    }


}

