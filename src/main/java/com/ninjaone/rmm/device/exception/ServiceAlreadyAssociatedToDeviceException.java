package com.ninjaone.rmm.device.exception;

public class ServiceAlreadyAssociatedToDeviceException extends RuntimeException{
    public ServiceAlreadyAssociatedToDeviceException(String msg){
        super(msg);
    }
}
