package com.ninjaone.rmm.service;

import com.ninjaone.rmm.device.DeviceEntity;
import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.AddServiceOutput;
import com.ninjaone.rmm.service.payload.GetServiceOutput;
import com.ninjaone.rmm.service.payload.ServiceDeviceOutput;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ServiceConverter {

    public ServiceEntity toEntity(AddServiceInput input) {
        final ServiceEntity service = new ServiceEntity();
        service.setName(input.name);
        service.setNameConcat(input.name.replaceAll("\\s", ""));
        service.setCost(input.cost);
        return service;
    }

    public GetServiceOutput toGetServiceOutput(ServiceEntity service) {
        return new GetServiceOutput(
                service.getId(),
                service.getName(),
                service.getCost(),
                service.getDevices()
                        .stream()
                        .map(this::toServiceDeviceOutput).collect(Collectors.toList())
        );
    }

    public AddServiceOutput toAddServiceOutput(ServiceEntity entity) {
        return new AddServiceOutput(
                entity.getId(),
                entity.getName(),
                entity.getCost());
    }

    private ServiceDeviceOutput toServiceDeviceOutput(DeviceEntity device) {
        return new ServiceDeviceOutput(device.getId(), device.getSystemName(), device.getDeviceType());
    }
}
