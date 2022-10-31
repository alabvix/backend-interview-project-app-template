package com.ninjaone.rmm.device;

import com.ninjaone.rmm.service.ServiceEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name="device")
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String systemName;

    private String systemNameConcat;

    private DeviceType deviceType;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "devices")
    private Set<ServiceEntity> services = new HashSet<>();

    public DeviceEntity(){}

    public DeviceEntity(Long id, String systemName, DeviceType deviceType, Set<ServiceEntity> services) {
        this.id = id;
        this.systemName = systemName;
        this.deviceType = deviceType;
        this.services = services;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public Set<ServiceEntity> getServices() {
        return services;
    }

    public void setServices(Set<ServiceEntity> services) {
        this.services = services;
    }

    public String getSystemNameConcat() {
        return systemNameConcat;
    }

    public void setSystemNameConcat(String systemNameConcat) {
        this.systemNameConcat = systemNameConcat;
    }

    public void addService(ServiceEntity service) {
        this.services.add(service);
    }

    public void removeService(ServiceEntity service) {
        this.services.remove(service);
        service.getDevices().remove(this);
    }

}
