package com.ninjaone.rmm.device.payload;

import com.ninjaone.rmm.device.DeviceType;

public class AddDeviceOutput {

    public final Long id;

    public final String systemName;

    public final DeviceType deviceType;

    public AddDeviceOutput(Long id, String systemName, DeviceType deviceType) {
        this.id = id;
        this.systemName = systemName;
        this.deviceType = deviceType;
    }

    public AddDeviceOutput() {
        this.id = 0L;
        this.systemName = "";
        this.deviceType = DeviceType.WINDOWS_WORKSTATION;
    }
}
