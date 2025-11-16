package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class HouseActiveRentException extends RuntimeException {
    public HouseActiveRentException(String houseId) {
        super("Cannot delete house with id '" + houseId + "' because it has active rents");
    }
}

