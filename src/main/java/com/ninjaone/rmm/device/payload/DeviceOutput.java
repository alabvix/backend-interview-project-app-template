package com.ninjaone.rmm.device.payload;

import com.ninjaone.rmm.device.DeviceType;

public class DeviceOutput {
    public final Long id;

    public final String systemName;

    public final DeviceType deviceType;

    public DeviceOutput(Long id, String systemName, DeviceType deviceType) {
        this.id = id;
        this.systemName = systemName;
        this.deviceType = deviceType;
    }
}
