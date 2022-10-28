package com.ninjaone.rmm.customer.entity;

import javax.persistence.*;
import java.util.List;

@Entity(name="customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy="customer")
    private List<CustomerDeviceEntity> devices;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CustomerDeviceEntity> getDevices() {
        return devices;
    }

    public void setDevices(List<CustomerDeviceEntity> devices) {
        this.devices = devices;
    }
}
