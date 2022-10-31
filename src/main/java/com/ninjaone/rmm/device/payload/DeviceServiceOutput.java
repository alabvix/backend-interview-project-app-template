package com.ninjaone.rmm.device.payload;

import java.math.BigDecimal;

public class DeviceServiceOutput {
    public final Long systemId;

    public final String name;

    public final BigDecimal cost;

    public DeviceServiceOutput() {
        this.systemId = 0L;
        this.name = "";
        this.cost = BigDecimal.ZERO;
    }

    public DeviceServiceOutput(Long systemId, String name, BigDecimal cost) {
        this.systemId = systemId;
        this.name = name;
        this.cost = cost;
    }
}
