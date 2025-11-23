package org.nbd.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;


public class UserNotFoundException extends AppBaseException {
    public UserNotFoundException(ObjectId id) {
        super(HttpStatus.NOT_FOUND, "Client with id " + id + " not found");
    }
    public UserNotFoundException(String login) {
        super(HttpStatus.NOT_FOUND, "Client with id " + login + " not found");
    }
}