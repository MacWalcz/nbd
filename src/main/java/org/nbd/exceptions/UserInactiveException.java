package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;


public class UserInactiveException extends AppBaseException {
    public UserInactiveException(String id) {
        super (HttpStatus.BAD_REQUEST, "User with id '" + id + "' is inactive");
    }
}
