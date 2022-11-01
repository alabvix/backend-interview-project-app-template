package com.ninjaone.rmm.service.payload;

import com.ninjaone.rmm.service.validation.ServiceName;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddServiceInput {

    @ServiceName
    @NotNull(message = "System Name cannot be null")
    @NotEmpty(message = "System Name cannot be empty")
    public final String name;

    @NotNull(message = "Service cost cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Service cost should be greater then zero")
    public final BigDecimal cost;

    public AddServiceInput() {
        this.name = "";
        this.cost = BigDecimal.ZERO;
    }

    public AddServiceInput(String name, BigDecimal cost) {
        this.name = name;
        this.cost = cost;
    }
}
