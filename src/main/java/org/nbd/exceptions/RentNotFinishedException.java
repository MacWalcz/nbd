package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RentNotFinishedException extends RuntimeException {
    public RentNotFinishedException(String id) {
        super("Can't delete the finished rent with id " + id);
    }
}



