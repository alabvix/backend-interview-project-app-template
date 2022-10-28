package com.ninjaone.rmm.device.rest;

import com.ninjaone.rmm.device.DeviceType;
import com.ninjaone.rmm.device.validation.DeviceSystemName;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class AddDevicePayload {

    @DeviceSystemName
    @NotNull(message = "System Name cannot be null")
    @NotEmpty(message = "System Name cannot be empty")
    public final String systemName;

    @NotNull(message = "Device Type cannot be null")
    public final DeviceType deviceType;

    public AddDevicePayload(){
        this.systemName = "";
        this.deviceType = null;
    }

    public AddDevicePayload(String systemName, DeviceType deviceType) {
        this.systemName = systemName;
        this.deviceType = deviceType;
    }

}
