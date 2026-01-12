package org.nbd.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;


public class UserInactiveException extends AppBaseException {
    public UserInactiveException(ObjectId id) {
        super (HttpStatus.BAD_REQUEST, "User with id " + id + " is inactive");
    }
}
