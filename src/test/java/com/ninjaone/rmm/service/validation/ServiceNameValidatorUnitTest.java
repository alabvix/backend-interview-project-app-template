package com.ninjaone.rmm.service.validation;

import com.ninjaone.rmm.device.DeviceEntity;
import com.ninjaone.rmm.device.DeviceRepository;
import com.ninjaone.rmm.device.DeviceType;
import com.ninjaone.rmm.device.validation.DeviceSystemName;
import com.ninjaone.rmm.device.validation.DeviceSystemNameValidator;
import com.ninjaone.rmm.service.ServiceEntity;
import com.ninjaone.rmm.service.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServiceNameValidatorUnitTest {

    @Mock
    private ServiceName systemName;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ServiceRepository repository;

    private ServiceNameValidator validator;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        validator = new ServiceNameValidator();
        validator.initialize(systemName);
        ReflectionTestUtils.setField(validator, "serviceRepository", repository);
    }

    @Test
    @DisplayName("Given a service with existing name should not be valid")
    public void isValid_givenDeviceWithExistingName_shouldNotBeValid() {

        final ServiceEntity service = new ServiceEntity();
        service.setId(1L);
        service.setName("Kernel Update");

        final String name = "KernelUpdate";

        when(repository.findByNameConcat(name)).thenReturn(Optional.of(service));

        assertFalse(validator.isValid(name, constraintValidatorContext));
        verify(repository).findByNameConcat(name);

    }

    @Test
    @DisplayName("Given a device with nom existent name should be valid")
    public void isValid_givenDeviceNonExistentName_shouldBeValid() {

        final ServiceEntity service = new ServiceEntity();
        service.setId(1L);
        service.setName("Kernel Update");

        final String name = "KernelUpdate";

        when(repository.findByNameConcat(name)).thenReturn(Optional.empty());

        assertTrue(validator.isValid(name, constraintValidatorContext));
        verify(repository).findByNameConcat(name);

    }
}
