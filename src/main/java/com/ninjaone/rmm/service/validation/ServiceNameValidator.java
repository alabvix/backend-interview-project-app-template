package com.ninjaone.rmm.service.validation;

import com.ninjaone.rmm.service.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ServiceNameValidator implements ConstraintValidator<ServiceName, String> {

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return serviceRepository.findByNameConcat(
                value.replaceAll("\\s", "")
        ).isEmpty();
    }
}
