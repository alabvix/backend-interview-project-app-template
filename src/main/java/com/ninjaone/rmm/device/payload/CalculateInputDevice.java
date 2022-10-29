package com.ninjaone.rmm.device.payload;

public class CalculateInputDevice {
    public final Long deviceId;
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
