package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.AddDeviceInput;
import com.ninjaone.rmm.device.payload.AddDeviceOutput;
import com.ninjaone.rmm.device.payload.GetDeviceOutput;
import com.ninjaone.rmm.service.ServiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviceConverterUnitTest {

    private DeviceConverter deviceConverter;

    @BeforeEach
    public void beforeEach(){
        deviceConverter = new DeviceConverter();
    }

    @Test
    @DisplayName("ToEntity")
    public void toEntity(){
        final AddDeviceInput payload =
                new AddDeviceInput("Windows 95", DeviceType.WINDOWS_WORKSTATION);

        final DeviceEntity entity = deviceConverter.toEntity(payload);

        assertEquals("Windows 95", entity.getSystemName());
        assertEquals(DeviceType.WINDOWS_WORKSTATION, entity.getDeviceType());

    }

    @Test
    @DisplayName("ToGetDeviceOutput")
    public void toGetDeviceOutput(){
        final DeviceEntity device = new DeviceEntity();
        device.setId(1L);
        device.setSystemName("Windows 98");
        device.setDeviceType(DeviceType.WINDOWS_WORKSTATION);

        final ServiceEntity service = new ServiceEntity();
        service.setId(1L);
        service.setCost(new BigDecimal("10.00"));
        service.setName("Windows Upgrade");

        device.addService(service);

        final GetDeviceOutput output = deviceConverter.toGetDeviceOutput(device);

        assertEquals(1L, output.id);
        assertEquals("Windows 98", output.systemName);
        assertEquals(DeviceType.WINDOWS_WORKSTATION, output.deviceType);

        assertEquals(1L, output.services.get(0).serviceId);
        assertEquals("Windows Upgrade", output.services.get(0).name);
        assertEquals(new BigDecimal("10.00"), output.services.get(0).cost);

    }

    @Test
    @DisplayName("ToAddDeviceOutput")
    public void toAddDeviceOutput(){
        final DeviceEntity device = new DeviceEntity();
        device.setId(1L);
        device.setSystemName("Windows 98");
        device.setDeviceType(DeviceType.WINDOWS_WORKSTATION);

        final AddDeviceOutput output = deviceConverter.toAddDeviceOutput(device);

        assertEquals(1L, output.id);
        assertEquals("Windows 98", output.systemName);
        assertEquals(DeviceType.WINDOWS_WORKSTATION, output.deviceType);

    }
}
