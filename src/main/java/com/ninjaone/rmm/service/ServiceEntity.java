package com.ninjaone.rmm.service;

import com.ninjaone.rmm.device.DeviceEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name="service")
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nameConcat;

    private BigDecimal cost;

    @ManyToMany
    @JoinTable(
            name = "service_device",
            joinColumns = { @JoinColumn(name = "service_id") },
            inverseJoinColumns = { @JoinColumn(name = "device_id") }
    )
    private Set<DeviceEntity> devices = new HashSet<>();

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

    public Set<DeviceEntity> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceEntity> devices) {
        this.devices = devices;
    }

    public String getNameConcat() {
        return nameConcat;
    }

    public void setNameConcat(String nameConcat) {
        this.nameConcat = nameConcat;
    }

    public void addDevice(DeviceEntity device){
        this.devices.add(device);
    }

    public void removeDevice(DeviceEntity device){
        this.devices.remove(device);
    }
}
