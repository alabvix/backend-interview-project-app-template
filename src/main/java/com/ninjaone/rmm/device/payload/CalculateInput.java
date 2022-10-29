package com.ninjaone.rmm.device.payload;

import java.util.List;

public class CalculateInput {
    private List<CalculateInputDevice> devices;

    public CalculateInput(){}

    public List<CalculateInputDevice> getDevices() {
        return devices;
    }

    public void setDevices(List<CalculateInputDevice> devices) {
        this.devices = devices;
    }
}
