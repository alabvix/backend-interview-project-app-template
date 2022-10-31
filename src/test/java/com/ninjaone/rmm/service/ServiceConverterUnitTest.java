package com.ninjaone.rmm.service;

import com.ninjaone.rmm.device.DeviceEntity;
import com.ninjaone.rmm.device.DeviceType;
import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.ServiceOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceConverterUnitTest {

    private ServiceConverter serviceConverter;

    @BeforeEach
    public void beforeEach(){
        serviceConverter = new ServiceConverter();
    }

    @Test
    public void toEntity() {
        final AddServiceInput input = new AddServiceInput("System Backup", new BigDecimal("10.00"));

        final ServiceEntity entity = serviceConverter.toEntity(input);

        assertEquals(input.name, entity.getName());
        assertEquals(input.cost, entity.getCost());
    }

    @Test
    public void toServiceOutput() {
        // given
        final ServiceEntity service = new ServiceEntity();
        service.setId(1L);
        service.setCost(new BigDecimal("10.00"));
        service.setName("Linux Upgrade");

        DeviceEntity device = new DeviceEntity();
        device.setId(1L);
        device.setSystemName("Ubuntu Linux");
        device.setDeviceType(DeviceType.LINUX_WORKSTATION);

        Set<DeviceEntity> devices = new HashSet<>();
        devices.add(device);
        service.setDevices(devices);

        // when
        final ServiceOutput output = serviceConverter.toServiceOutput(service);

        // then
        assertEquals(service.getId(), output.serviceId);
        assertEquals(service.getName(), output.name);
        assertEquals(service.getCost(), output.cost);
        assertEquals(1, output.devices.size());

        assertEquals(device.getId(), output.devices.get(0).id);
        assertEquals(device.getDeviceType(), output.devices.get(0).deviceType);
        assertEquals(device.getSystemName(), output.devices.get(0).systemName);

    }
}
