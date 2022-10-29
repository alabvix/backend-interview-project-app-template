package com.ninjaone.rmm.device.payload;

import com.ninjaone.rmm.device.DeviceType;
import com.ninjaone.rmm.device.validation.DeviceSystemName;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AddDeviceInput {

    @DeviceSystemName
    @NotNull(message = "System Name cannot be null")
    @NotEmpty(message = "System Name cannot be empty")
    public final String systemName;

    @NotNull(message = "Device Type cannot be null")
    public final DeviceType deviceType;

    public final List<Long> servicesId;

    public AddDeviceInput(){
        this.systemName = "";
        this.deviceType = null;
        this.servicesId = new ArrayList<>();
    }

    public AddDeviceInput(String systemName, DeviceType deviceType, List<Long> serviceId) {
        this.systemName = systemName;
        this.deviceType = deviceType;
        this.servicesId = serviceId;
    }

}
