package com.ninjaone.rmm.device.payload;

import java.math.BigDecimal;

public class DeviceServiceOutput {
    public final Long serviceId;

    public final String name;

    public final BigDecimal cost;

    public DeviceServiceOutput() {
        this.serviceId = 0L;
        this.name = "";
        this.cost = BigDecimal.ZERO;
    }

    public DeviceServiceOutput(Long serviceId, String name, BigDecimal cost) {
        this.serviceId = serviceId;
        this.name = name;
        this.cost = cost;
    }

    public Long getServiceId() {
        return serviceId;
    }
}
