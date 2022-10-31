package com.ninjaone.rmm.device.payload;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class RemoveAssociatedServicesInput {

    @NotNull(message = "Device Id cannot be null")
    public final Long deviceId;

    @NotNull(message = "Services Id cannot be null")
    public final List<Long> servicesId;

    public RemoveAssociatedServicesInput() {
        this.deviceId = 0L;
        this.servicesId = new ArrayList<>();
    }

    public RemoveAssociatedServicesInput(Long deviceId, List<Long> servicesId) {
        this.deviceId = deviceId;
        this.servicesId = servicesId;
    }
}
