package com.ninjaone.rmm.customer;

import com.ninjaone.rmm.customer.entity.CustomerDeviceEntity;
import com.ninjaone.rmm.customer.entity.CustomerEntity;
import com.ninjaone.rmm.device.DeviceEntity;
import com.ninjaone.rmm.device.DeviceType;
import com.ninjaone.rmm.service.ServiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerServiceUnitTest {

    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository);
    }

    @DisplayName("First Test")
    @Test
    public void calculateCost() {

        // given
        CustomerEntity customer = new CustomerEntity();
        customer.setName("Ninja One");
        DeviceEntity deviceWindows =
                new DeviceEntity(1L, "Windows 10", DeviceType.WINDOWS_WORKSTATION, getWindowsServices());
        DeviceEntity deviceMac =
                new DeviceEntity(2L, "MacBook Pro", DeviceType.MAC, getMacServices());

        customer.setDevices(List.of(
                new CustomerDeviceEntity(1L, customer, deviceWindows, 2),
                new CustomerDeviceEntity(2L, customer, deviceMac, 3)
        ));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // When
        BigDecimal totalCost = customerService.calculateCost(1L);

        // then
        assertEquals(new BigDecimal("71"), totalCost);
        verify(customerRepository, times(1)).findById(1L);

    }

    private List<ServiceEntity> getWindowsServices() {
        return List.of(
                new ServiceEntity(1L, "Device Cost", new BigDecimal("4.00")),
                new ServiceEntity(2L, "Backup", new BigDecimal("3.00")),
                new ServiceEntity(3L, "Screen Share", new BigDecimal("1.00")),
                new ServiceEntity(4L, "Antivirus Windows", new BigDecimal("5.00"))
        );
    }

    private List<ServiceEntity> getMacServices() {
        return List.of(
                new ServiceEntity(1L, "Device Cost", new BigDecimal("4.00")),
                new ServiceEntity(2L, "Backup", new BigDecimal("3.00")),
                new ServiceEntity(3L, "Screen Share", new BigDecimal("1.00")),
                new ServiceEntity(5L, "Antivirus Mac", new BigDecimal("7.00"))
        );
    }

}

