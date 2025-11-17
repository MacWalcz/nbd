package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class HouseNotFoundException extends AppBaseException {
    public HouseNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND,"House with id '" + id + "' not found");
    }
}