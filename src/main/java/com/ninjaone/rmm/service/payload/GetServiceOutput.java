package com.ninjaone.rmm.service.payload;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GetServiceOutput {
    public final Long serviceId;

    public final String name;

    public final BigDecimal cost;

    public final List<ServiceDeviceOutput> devices;

    public GetServiceOutput() {
        this.serviceId = 0L;
        this.name = "";
        this.cost = BigDecimal.ZERO;
        this.devices = new ArrayList<>();
    }

    public GetServiceOutput(Long serviceId, String name, BigDecimal cost, List<ServiceDeviceOutput> devices) {
        this.serviceId = serviceId;
        this.name = name;
        this.cost = cost;
        this.devices = devices;
    }
}
