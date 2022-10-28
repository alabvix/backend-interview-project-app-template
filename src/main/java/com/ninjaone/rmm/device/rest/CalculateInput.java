package com.ninjaone.rmm.device.rest;

import java.util.List;

public class CalculateInput {
    private List<DeviceInput> devices;

    public CalculateInput(){}

    public List<DeviceInput> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceInput> devices) {
        this.devices = devices;
    }
}
