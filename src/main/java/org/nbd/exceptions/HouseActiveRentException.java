package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;


public class HouseActiveRentException extends AppBaseException {
    public HouseActiveRentException(String houseId) {
        super(HttpStatus.CONFLICT, "House is already rented");
    }
}

