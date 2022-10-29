package com.ninjaone.rmm.device.payload;

import java.math.BigDecimal;
import java.util.List;

public class CalculateDetailedOutput {
    public final BigDecimal total;

    public final List<CalculateDetailedDeviceOutput> devices;

    public CalculateDetailedOutput(BigDecimal total, List<CalculateDetailedDeviceOutput> devices) {
        this.total = total;
        this.devices = devices;
    }
}
