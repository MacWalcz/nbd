package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


public class UserNotFoundException extends AppBaseException {
    public UserNotFoundException(String id) {
        super(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
    }
}