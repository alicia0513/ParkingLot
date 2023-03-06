package com.aliciatsai.parkinglot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public final class InvalidVehicleException extends RuntimeException {
    public InvalidVehicleException() {
        super();
    }
    public InvalidVehicleException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidVehicleException(String message) {
        super(message);
    }
    public InvalidVehicleException(Throwable cause) {
        super(cause);
    }
}
