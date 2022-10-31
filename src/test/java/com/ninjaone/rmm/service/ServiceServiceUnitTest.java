package com.ninjaone.rmm.service;

import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.AddServiceOutput;
import com.ninjaone.rmm.service.payload.GetServiceOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServiceServiceUnitTest {

    private ServiceService serviceService;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ServiceConverter serviceConverter;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        serviceService = new ServiceService(serviceRepository, serviceConverter);
    }

    @Test
    @DisplayName("AddService: Given a valid service input should create a service.")
    public void addService_givenValidInput_shouldCreateService() {

        // given
        AddServiceInput input = new AddServiceInput("Ubuntu Update", new BigDecimal("10.54"));
        AddServiceOutput expectedOutput = new AddServiceOutput(1L,"Ubuntu Update",
                new BigDecimal("10.54"));

        final ServiceEntity service = new ServiceEntity(null, "Ubuntu Update", new BigDecimal("10.54"));

        final ServiceEntity savedService = new ServiceEntity(1L, "Ubuntu Update", new BigDecimal("10.54"));

        when(serviceConverter.toEntity(input)).thenReturn(service);
        when(serviceConverter.toAddServiceOutput(savedService)).thenReturn(expectedOutput);
        when(serviceRepository.save(service)).thenReturn(savedService);

        // when
        final AddServiceOutput output = serviceService.addService(input);

        // then
        assertEquals(1L, output.serviceId);
        assertEquals("Ubuntu Update", output.name);
        assertEquals( new BigDecimal("10.54"), output.cost);

        verify(serviceRepository, times(1)).save(service);
        verify(serviceConverter, times(1)).toEntity(input);
        verify(serviceConverter, times(1)).toAddServiceOutput(savedService);

    }

    @Test
    @DisplayName("DeleteService: Given a valid service input should delete.")
    public void deleteService_validId_shouldDelete(){
        // given
        final Long serviceCode = 1L;
        final ServiceEntity service = new ServiceEntity(null, "Ubuntu Update", new BigDecimal("10.54"));

        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));

        // when
        serviceService.deleteService(serviceCode);

        // then
        verify(serviceRepository, times(1)).delete(service);

    }

    @Test
    @DisplayName("GetServiceById: Given a valid service id return a service.")
    public void getServiceById_validServiceId_shouldReturnService() {
        // given
        final ServiceEntity service = new ServiceEntity(1L, "Windows Server Configuration",
                new BigDecimal("10.00"));

        final GetServiceOutput output = new GetServiceOutput(1L, "Windows Server Configuration",
                new BigDecimal("10.00"), new ArrayList<>());

        when(serviceConverter.toGetServiceOutput(service)).thenReturn(output);
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));

        // when
        GetServiceOutput serviceOutput = serviceService.getServiceById(1L);

        // then
        assertEquals(output.serviceId, serviceOutput.serviceId);
        assertEquals(output.name, serviceOutput.name);
        assertEquals(output.cost, serviceOutput.cost);
        verify(serviceRepository, times(1)).findById(1L);

    }

    @Test
    @DisplayName("GetServiceById: Given a not found service id should throws a service not found exception.")
    public void getServiceById_notFoundId_shouldThrowsServiceNotFoundException() {
        // given
        when(serviceRepository.findById(2L)).thenReturn(Optional.empty());

        // when
        ServiceNotFoundException exception = assertThrows(
                ServiceNotFoundException.class,
                () -> serviceService.getServiceById(2L)
        );

        // then
        assertEquals("Service with id 2 not found",exception.getMessage());
        verify(serviceRepository, times(1)).findById(2L);

    }

}
