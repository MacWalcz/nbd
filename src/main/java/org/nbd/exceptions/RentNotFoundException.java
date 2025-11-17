package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;


public class RentNotFoundException extends AppBaseException {
    public RentNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, "Rent with id '" + id + "' not found");
    }
}