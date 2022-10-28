package com.ninjaone.rmm.device.rest;

import java.math.BigDecimal;
import java.util.List;

public class DeviceOutput {
    public final Long deviceId;

    public final String deviceName;

    public final BigDecimal cost;

    public final List<DeviceServiceOutput> services;

    public DeviceOutput() {
        this.cost = BigDecimal.ZERO;
        this.deviceId = 0L;
        this.deviceName = "";
        this.services = List.of();
    }

    public DeviceOutput(Long deviceId, String deviceName, BigDecimal cost, List<DeviceServiceOutput> services) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.cost = cost;
        this.services = services;
    }
}
