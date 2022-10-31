package com.ninjaone.rmm.device.payload;

import java.math.BigDecimal;

public class CalculateOutput {
    public final BigDecimal total;

    public CalculateOutput(){
        total = BigDecimal.ZERO;
    }

    public CalculateOutput(BigDecimal total) {
        this.total = total;
    }
}
