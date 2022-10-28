package com.ninjaone.rmm.customer.entity;

import com.ninjaone.rmm.device.DeviceEntity;

import javax.persistence.*;

@Entity(name="customerDevice")
public class CustomerDeviceEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private CustomerEntity customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "device_id", referencedColumnName = "id")
    private DeviceEntity device;

    private Integer quantity;

    public CustomerDeviceEntity(Long id, CustomerEntity customer, DeviceEntity device, Integer quantity) {
        this.id = id;
        this.customer = customer;
        this.device = device;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public DeviceEntity getDevice() {
        return device;
    }

    public void setDevice(DeviceEntity device) {
        this.device = device;
    }
}
