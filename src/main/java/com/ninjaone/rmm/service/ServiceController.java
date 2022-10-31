package com.ninjaone.rmm.service;

import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.GetServiceOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/service")
@Api(tags = "Service")
public class ServiceController {

    private final ServiceService service;

    public ServiceController(ServiceService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "This method is used to get a service by your id.")
    public ResponseEntity<GetServiceOutput> getService(@PathVariable String id){
        return new ResponseEntity<>(service.getService(Long.parseLong(id)), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "This method is used to add a new Service.")
    public ResponseEntity<GetServiceOutput> addDevice(@Valid @RequestBody AddServiceInput input){
        return new ResponseEntity<>(
                service.addService(input),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "This method is used to delete a service.")
    private void deleteService(@PathVariable String id){
        service.deleteService(Long.parseLong(id));
    }

}
