package com.ninjaone.rmm.customer;

import com.ninjaone.rmm.customer.entity.CustomerDeviceEntity;
import com.ninjaone.rmm.customer.entity.CustomerEntity;
import com.ninjaone.rmm.device.DeviceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public BigDecimal calculateCost(Long customerId) {

        Optional<CustomerEntity> customerOp = customerRepository.findById(customerId);
        if (customerOp.isEmpty()) {
            throw new RuntimeException("Customer not found for id " + customerId);
        }

        List<CustomerDeviceEntity> devices = customerOp.get().getDevices();
        BigDecimal totalCustomer = BigDecimal.ZERO;
        final MathContext mathContext = new MathContext(2);

        for (CustomerDeviceEntity customerDevice: devices) {
            final DeviceEntity device = customerDevice.getDevice();

            BigDecimal totalCost = device.getServices().stream()
                    .map(s -> s.getCost().multiply(BigDecimal.valueOf(customerDevice.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            totalCustomer = totalCustomer.add(totalCost, mathContext);
        }

        return totalCustomer;
    }

}
