package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.rest.AddDevicePayload;
import com.ninjaone.rmm.device.rest.DeviceResponse;
import org.springframework.stereotype.Component;

@Component
public class DeviceConverter {

    public DeviceEntity toEntity(AddDevicePayload payload) {
        final DeviceEntity device = new DeviceEntity();
        device.setDeviceType(payload.deviceType);
        device.setSystemName(payload.systemName);
        device.setSystemNameConcat(payload.systemName.replaceAll("\\s", ""));
        return device;
    }

    public DeviceResponse toDeviceResponse(DeviceEntity entity) {
        return new DeviceResponse(entity.getId(), entity.getSystemName(), entity.getDeviceType());
    }

}
