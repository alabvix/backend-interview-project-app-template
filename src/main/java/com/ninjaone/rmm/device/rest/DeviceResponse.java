package com.ninjaone.rmm.device.rest;

import com.ninjaone.rmm.device.DeviceType;

public class DeviceResponse {
    public final Long id;

    public final String systemName;

    public final DeviceType deviceType;

    public DeviceResponse(Long id, String systemName, DeviceType deviceType) {
        this.id = id;
        this.systemName = systemName;
        this.deviceType = deviceType;
    }
}
