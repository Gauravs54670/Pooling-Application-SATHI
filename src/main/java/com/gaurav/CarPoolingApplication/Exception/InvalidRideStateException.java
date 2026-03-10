package com.gaurav.CarPoolingApplication.Exception;

public class InvalidRideStateException extends RuntimeException {
    public InvalidRideStateException(String message) {
        super(message);
    }
}
