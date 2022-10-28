package com.ninjaone.rmm.device.rest;

import java.math.BigDecimal;

public class DeviceServiceOutput {
    public final Long serviceId;
    public final String serviceName;
    public final BigDecimal totalCost;

    public DeviceServiceOutput(Long serviceId, String serviceName, BigDecimal totalCost) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.totalCost = totalCost;
    }
}
