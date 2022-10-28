package com.ninjaone.rmm.device.validation;

import com.ninjaone.rmm.device.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DeviceSystemNameValidator implements ConstraintValidator<DeviceSystemName, String> {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return deviceRepository.findBySystemNameConcat(
                value.replaceAll("\\s", "")
        ).isEmpty();
    }
}
