package com.ninjaone.rmm.device.rest;

import com.ninjaone.rmm.device.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DeviceResponse> addDevice(@Valid @RequestBody AddDevicePayload payload){
        return new ResponseEntity<>(deviceService.saveDevice(payload), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable String id){
        return new ResponseEntity<DeviceResponse>(deviceService.getDevice(Long.parseLong(id)), HttpStatus.OK);
    }

//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    private void deleteDevice(@PathVariable String id){
//        sampleService.deleteSampleEntity(id);
//    }

}
