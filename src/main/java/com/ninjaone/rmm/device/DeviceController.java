package com.ninjaone.rmm.device;

import com.ninjaone.rmm.device.payload.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/device")
@Api(tags = "Device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "This method is used to add a new Device.")
    public ResponseEntity<AddDeviceOutput> addDevice(@Valid @RequestBody AddDeviceInput payload){
        return new ResponseEntity<>(
                deviceService.addDevice(payload),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "This method is used to get a device by your id.")
    public ResponseEntity<GetDeviceOutput> getDevice(@PathVariable String id){
        return new ResponseEntity<GetDeviceOutput>(
                deviceService.getDeviceById(Long.parseLong(id)),
                HttpStatus.OK);
    }

    @PostMapping("/calculate")
    @ApiOperation(value = "This method is used to calculate the total monthly cost by device.")
    public ResponseEntity<CalculateOutput> calculateCost(@RequestBody CalculateInput payload){
        return new ResponseEntity<>(
                deviceService.calculateCost(payload),
                HttpStatus.OK);
    }

    @PostMapping("/calculate/detailed")
    @ApiOperation(value = "This method is used to calculate the total monthly cost by device with detailed response.")
    public ResponseEntity<CalculateDetailedOutput> calculateCostDetailed(@RequestBody CalculateInput payload){
        return new ResponseEntity<>(
                deviceService.calculateCostDetailed(payload),
                HttpStatus.OK);
    }

    @PutMapping("/")
    @ApiOperation(value = "This method is used to update an existent device.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDevice(@Valid @RequestBody UpdateDeviceInput input){
        deviceService.updateDevice(input);
    }

    @PutMapping("/associate/services")
    @ApiOperation(value = "This method is used to associate existent services to an existent device.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associateServices(@RequestBody AssociateDeviceServicesInput payload){
        deviceService.associateServices(payload);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "This method is used to delete a device.")
    private void deleteDevice(@PathVariable String id){
        deviceService.deleteDevice(Long.parseLong(id));
    }

}
