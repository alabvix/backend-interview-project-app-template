package com.ninjaone.rmm.device.payload;

import com.ninjaone.rmm.device.DeviceType;
import com.ninjaone.rmm.device.validation.DeviceSystemName;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateDeviceInput {

    @NotNull(message = "System Name cannot be null")
    public final Long id;

    @DeviceSystemName
    @NotNull(message = "System Name cannot be null")
    @NotEmpty(message = "System Name cannot be empty")
    public final String systemName;

    @NotNull(message = "Device Type cannot be null")
    public final DeviceType deviceType;

    public UpdateDeviceInput(Long id, String systemName, DeviceType deviceType) {
        this.id = id;
        this.systemName = systemName;
        this.deviceType = deviceType;
    }
}
