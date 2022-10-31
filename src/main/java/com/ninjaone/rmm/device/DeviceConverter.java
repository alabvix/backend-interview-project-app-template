package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.AddDeviceInput;
import com.ninjaone.rmm.device.payload.AddDeviceOutput;
import com.ninjaone.rmm.device.payload.DeviceOutput;
import com.ninjaone.rmm.device.payload.DeviceServiceOutput;
import com.ninjaone.rmm.service.ServiceEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DeviceConverter {

    public DeviceEntity toEntity(AddDeviceInput payload) {
        final DeviceEntity device = new DeviceEntity();
        device.setDeviceType(payload.deviceType);
        device.setSystemName(payload.systemName);
        device.setSystemNameConcat(payload.systemName.replaceAll("\\s", ""));
        return device;
    }

    public DeviceOutput toDeviceOutput(DeviceEntity entity) {
        return new DeviceOutput(
                entity.getId(),
                entity.getSystemName(),
                entity.getDeviceType(),
                entity.getServices()
                        .stream()
                        .map(this::toDeviceServiceOutput).collect(Collectors.toList())
        );
    }

    public AddDeviceOutput toAddDeviceOutput(DeviceEntity entity) {
        return new AddDeviceOutput(
                entity.getId(),
                entity.getSystemName(),
                entity.getDeviceType());
    }

    private DeviceServiceOutput toDeviceServiceOutput(ServiceEntity service) {
        return new DeviceServiceOutput(service.getId(), service.getName(), service.getCost());
    }

}
