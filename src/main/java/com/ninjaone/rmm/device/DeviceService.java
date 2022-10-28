package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.rest.AddDevicePayload;
import com.ninjaone.rmm.device.rest.DeviceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final DeviceConverter deviceConverter;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, DeviceConverter deviceConverter) {
        this.deviceRepository = deviceRepository;
        this.deviceConverter = deviceConverter;
    }

    public DeviceResponse saveDevice(AddDevicePayload payload){
        final DeviceEntity device = deviceRepository.save(
                deviceConverter.toEntity(payload)
        );

        return deviceConverter.toDeviceResponse(device);
    }

    public DeviceResponse getDevice(Long deviceId){

        Optional<DeviceEntity> optional = deviceRepository.findById(deviceId);
        if (optional.isEmpty()){
            throw new DeviceNotFoundException("Device with id " + deviceId + " not found");
        }

        return deviceConverter.toDeviceResponse(optional.get());
    }


}

