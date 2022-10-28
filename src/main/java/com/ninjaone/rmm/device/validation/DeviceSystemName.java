package com.ninjaone.rmm.device.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DeviceSystemNameValidator.class)
public @interface DeviceSystemName {

    String message() default "Device with given system name already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
