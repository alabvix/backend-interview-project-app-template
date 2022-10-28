package com.ninjaone.rmm.device.rest;

public class DeviceInput {
    public final Long deviceId;
    public final int quantity;

    public DeviceInput(){
        deviceId = 0L;
        quantity = 0;
    }

    public DeviceInput(Long deviceId, int quantity) {
        this.deviceId = deviceId;
        this.quantity = quantity;
    }
}
