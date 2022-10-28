package com.ninjaone.rmm.device;

public class DeviceNotFoundException extends RuntimeException{
    public DeviceNotFoundException(String msg){
        super(msg);
    }
}
