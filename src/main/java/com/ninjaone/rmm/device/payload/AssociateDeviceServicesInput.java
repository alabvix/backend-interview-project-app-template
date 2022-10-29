package com.ninjaone.rmm.device.payload;

import java.util.ArrayList;
import java.util.List;

public class AssociateDeviceServicesInput {

    public final Long deviceId;

    public final List<Long> servicesId;

    public AssociateDeviceServicesInput() {
        this.deviceId = 0L;
        this.servicesId = new ArrayList<>();
    }

    public AssociateDeviceServicesInput(Long deviceId, List<Long> servicesId) {
        this.deviceId = deviceId;
        this.servicesId = servicesId;
    }
}
