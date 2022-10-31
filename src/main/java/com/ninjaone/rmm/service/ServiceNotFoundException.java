package com.ninjaone.rmm.service;

public class ServiceNotFoundException extends RuntimeException{
    public ServiceNotFoundException(String msg) {
        super(msg);
    }
}
