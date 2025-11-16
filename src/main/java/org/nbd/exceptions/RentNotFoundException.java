package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RentNotFoundException extends RuntimeException {
    public RentNotFoundException(String id) {
        super("Rent with id '" + id + "' not found");
    }
}