package com.ninjaone.rmm.device.validation;

import com.ninjaone.rmm.device.DeviceEntity;
import com.ninjaone.rmm.device.DeviceRepository;
import com.ninjaone.rmm.device.DeviceType;
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

public class DeviceSystemNameValidatorUnitTest {

    @Mock
    private DeviceSystemName systemName;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private DeviceRepository repository;

    private DeviceSystemNameValidator validator;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
        validator = new DeviceSystemNameValidator();
        validator.initialize(systemName);
        ReflectionTestUtils.setField(validator, "deviceRepository", repository);
    }

    @Test
    @DisplayName("Given a device with existing name should not be valid")
    public void isValid_givenDeviceWithExistingName_shouldNotBeValid() {

        final DeviceEntity device = new DeviceEntity();
        device.setId(1L);
        device.setSystemName("Windows 98");
        device.setDeviceType(DeviceType.WINDOWS_WORKSTATION);

        final String systemNameConcat = "Windows98";

        when(repository.findBySystemNameConcat(systemNameConcat)).thenReturn(Optional.of(device));

        assertFalse(validator.isValid(systemNameConcat, constraintValidatorContext));
        verify(repository).findBySystemNameConcat(systemNameConcat);

    }

    @Test
    @DisplayName("Given a device with nom existent name should be valid")
    public void isValid_givenDeviceNonExistentName_shouldBeValid() {

        final DeviceEntity device = new DeviceEntity();
        device.setId(1L);
        device.setSystemName("Windows 98");
        device.setDeviceType(DeviceType.WINDOWS_WORKSTATION);

        final String systemNameConcat = "Windows98";

        when(repository.findBySystemNameConcat(systemNameConcat)).thenReturn(Optional.empty());

        assertTrue(validator.isValid(systemNameConcat, constraintValidatorContext));
        verify(repository).findBySystemNameConcat(systemNameConcat);

    }

}
