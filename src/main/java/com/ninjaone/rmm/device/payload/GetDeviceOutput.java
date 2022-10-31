package com.ninjaone.rmm.device.payload;

import com.ninjaone.rmm.device.DeviceType;

import java.util.List;

public class GetDeviceOutput {
    public final Long id;

    public final String systemName;

    public final DeviceType deviceType;

    public final List<DeviceServiceOutput> services;

    public GetDeviceOutput(Long id, String systemName, DeviceType deviceType, List<DeviceServiceOutput> services) {
        this.id = id;
        this.systemName = systemName;
        this.deviceType = deviceType;
        this.services = services;
    }
}
