package com.ninjaone.rmm.device.payload;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CalculateInputDevice {
    @NotNull(message = "Device Id can not be null")
    public final Long deviceId;

    @Min(1)
    public final int quantity;

    public CalculateInputDevice(){
        deviceId = 0L;
        quantity = 0;
    }

    public CalculateInputDevice(Long deviceId, int quantity) {
        this.deviceId = deviceId;
        this.quantity = quantity;
    }
}
