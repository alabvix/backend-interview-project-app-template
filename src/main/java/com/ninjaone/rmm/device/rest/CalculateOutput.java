package com.ninjaone.rmm.device.rest;

import java.math.BigDecimal;
import java.util.List;

public class CalculateOutput {
    public final BigDecimal total;

    public final List<DeviceOutput> devices;

    public CalculateOutput(BigDecimal total, List<DeviceOutput> devices) {
        this.total = total;
        this.devices = devices;
    }
}
