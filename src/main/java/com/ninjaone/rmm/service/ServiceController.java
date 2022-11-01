package com.ninjaone.rmm.service;

import com.ninjaone.rmm.service.payload.AddServiceInput;
import com.ninjaone.rmm.service.payload.AddServiceOutput;
import com.ninjaone.rmm.service.payload.GetServiceOutput;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Service Not found"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<GetServiceOutput> getService(@PathVariable String id){
        return new ResponseEntity<>(service.getServiceById(Long.parseLong(id)), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "This method is used to add a new Service.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")})
    public ResponseEntity<AddServiceOutput> addDevice(@Valid @RequestBody AddServiceInput input){
        return new ResponseEntity<>(
                service.addService(input),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "This method is used to delete a service.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Not content"),
            @ApiResponse(code = 404, message = "Service Not found"),
            @ApiResponse(code = 403, message = "Unauthorized"),
            @ApiResponse(code = 500, message = "Internal server error")})
    private void deleteService(@PathVariable String id){
        service.deleteService(Long.parseLong(id));
    }

}
