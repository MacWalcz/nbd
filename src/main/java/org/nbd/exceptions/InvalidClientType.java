package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidClientType extends RuntimeException {
    public InvalidClientType(String id) {
        super("Invalid client type: " + id);
    }
}


