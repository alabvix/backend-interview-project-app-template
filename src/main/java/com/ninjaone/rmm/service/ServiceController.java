package com.ninjaone.rmm.service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
@Api(tags = "Service")
public class ServiceController {

    @GetMapping("/{id}")
    @ApiOperation(value = "This method is used to get a service by yours id.")
    public ResponseEntity<String> getService(@PathVariable String id){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
