package com.ninjaone.rmm.service.payload;

import java.math.BigDecimal;

public class AddServiceOutput {

    public final Long serviceId;

    public final String name;

    public final BigDecimal cost;

    public AddServiceOutput() {
        this.serviceId = 0L;
        this.name = "";
        this.cost = BigDecimal.ZERO;
    }

    public AddServiceOutput(Long serviceId, String name, BigDecimal cost) {
        this.serviceId = serviceId;
        this.name = name;
        this.cost = cost;
    }
}
