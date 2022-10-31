package com.ninjaone.rmm.service.payload;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ServiceOutput {
    public final Long serviceId;

    public final String name;

    public final BigDecimal cost;

    public final List<ServiceDeviceOutput> devices;

    public ServiceOutput() {
        this.serviceId = 0L;
        this.name = "";
        this.cost = BigDecimal.ZERO;
        this.devices = new ArrayList<>();
    }

    public ServiceOutput(Long serviceId, String name, BigDecimal cost, List<ServiceDeviceOutput> devices) {
        this.serviceId = serviceId;
        this.name = name;
        this.cost = cost;
        this.devices = devices;
    }
}
