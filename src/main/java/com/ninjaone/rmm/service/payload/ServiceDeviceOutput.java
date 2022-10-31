package com.ninjaone.rmm.service.payload;

import com.ninjaone.rmm.device.DeviceType;

public class ServiceDeviceOutput {

    public final Long id;

    public final String systemName;

    public final DeviceType deviceType;

    public ServiceDeviceOutput() {
        this.id = 0L;
        this.systemName = "";
        this.deviceType = DeviceType.WINDOWS_WORKSTATION;
    }

    public ServiceDeviceOutput(Long id, String systemName, DeviceType deviceType) {
        this.id = id;
        this.systemName = systemName;
        this.deviceType = deviceType;
    }
}
