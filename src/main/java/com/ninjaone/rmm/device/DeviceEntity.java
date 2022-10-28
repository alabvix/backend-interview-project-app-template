package com.ninjaone.rmm.device;

import com.ninjaone.rmm.service.ServiceEntity;

import javax.persistence.*;
import java.util.List;

@Entity(name="device")
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String systemName;

    private String systemNameConcat;

    private DeviceType deviceType;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "device_service",
            joinColumns = { @JoinColumn(name = "device_id") },
            inverseJoinColumns = { @JoinColumn(name = "service_id") }
    )
    List<ServiceEntity> services;

    public DeviceEntity(){}

    public DeviceEntity(Long id, String systemName, DeviceType deviceType, List<ServiceEntity> services) {
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

    public List<ServiceEntity> getServices() {
        return services;
    }

    public void setServices(List<ServiceEntity> services) {
        this.services = services;
    }

    public String getSystemNameConcat() {
        return systemNameConcat;
    }

    public void setSystemNameConcat(String systemNameConcat) {
        this.systemNameConcat = systemNameConcat;
    }
}
