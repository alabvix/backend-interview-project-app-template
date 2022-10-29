package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.AddDeviceInput;
import com.ninjaone.rmm.device.payload.DeviceOutput;
import org.springframework.stereotype.Component;

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
        return new DeviceOutput(entity.getId(), entity.getSystemName(), entity.getDeviceType());
    }

}
