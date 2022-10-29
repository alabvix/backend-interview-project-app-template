package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.*;
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
    public ResponseEntity<DeviceOutput> addDevice(@Valid @RequestBody AddDeviceInput payload){
        return new ResponseEntity<>(
                deviceService.addDevice(payload),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceOutput> getDevice(@PathVariable String id){
        return new ResponseEntity<DeviceOutput>(
                deviceService.getDeviceById(Long.parseLong(id)),
                HttpStatus.OK);
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculateOutput> calculateCost(@RequestBody CalculateInput payload){
        return new ResponseEntity<>(
                deviceService.calculateCost(payload),
                HttpStatus.OK);
    }

    @PostMapping("/calculate/detailed")
    public ResponseEntity<CalculateDetailedOutput> calculateCostDetailed(@RequestBody CalculateInput payload){
        return new ResponseEntity<>(
                deviceService.calculateCostDetailed(payload),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    private void deleteDevice(@PathVariable String id){
        deviceService.deleteDevice(Long.parseLong(id));
    }

}
