package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserInactiveException extends RuntimeException {
    public UserInactiveException(String id) {
        super("User with id '" + id + "' is inactive");
    }
}
