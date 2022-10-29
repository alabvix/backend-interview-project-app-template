package com.ninjaone.rmm.device.payload;

import java.math.BigDecimal;

public class CalculateDetailedServiceOutput {
    public final Long serviceId;
    public final String serviceName;
    public final BigDecimal totalCost;

    public CalculateDetailedServiceOutput(Long serviceId, String serviceName, BigDecimal totalCost) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.totalCost = totalCost;
    }
}
