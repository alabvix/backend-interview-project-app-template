package com.ninjaone.rmm.device;

import com.ninjaone.rmm.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DeviceServiceUnitTest {

    private DeviceService deviceService;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private DeviceConverter deviceConverter;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        deviceService = new DeviceService(deviceRepository, serviceRepository, deviceConverter);
    }

    @Test
    public void calculateCost(){

    }

    @Test
    public void calculateCostDetailed(){

    }

    @Test
    public void addDevice(){

    }

    @Test
    public void deleteDevice(){

    }
}
