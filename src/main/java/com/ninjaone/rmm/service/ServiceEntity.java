package com.ninjaone.rmm.service;

import com.ninjaone.rmm.device.DeviceEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name="service")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal cost;

//    @ManyToMany(mappedBy = "services")
//    private List<DeviceEntity> devices = new ArrayList<>();

    public ServiceEntity(){}

    public ServiceEntity(Long id, String name, BigDecimal cost) {
        this.id = id;
        this.name = name;
        this.cost = cost;
    }

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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

//    public List<DeviceEntity> getDevices() {
//        return devices;
//    }
//
//    public void setDevices(List<DeviceEntity> devices) {
//        this.devices = devices;
//    }
}
