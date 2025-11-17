package org.nbd.exceptions;

import org.springframework.http.HttpStatus;

public class HouseNotAvaibleException extends AppBaseException {
    public HouseNotAvaibleException(String id) {
        super(HttpStatus.CONFLICT, "House " + id + " is not available.");
    }
}
