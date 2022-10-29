package com.ninjaone.rmm.device.payload;

import java.math.BigDecimal;
import java.util.List;

public class CalculateDetailedDeviceOutput {
    public final Long deviceId;

    public final String deviceName;

    public final BigDecimal cost;

    public final List<CalculateDetailedServiceOutput> services;

    public CalculateDetailedDeviceOutput() {
        this.cost = BigDecimal.ZERO;
        this.deviceId = 0L;
        this.deviceName = "";
        this.services = List.of();
    }

    public CalculateDetailedDeviceOutput(Long deviceId,
                                         String deviceName,
                                         BigDecimal cost,
                                         List<CalculateDetailedServiceOutput> services) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.cost = cost;
        this.services = services;
    }
}
