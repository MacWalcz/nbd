package org.nbd.exceptions;

import org.bson.types.ObjectId;
import jakarta.ws.rs.core.Response;


public class UserInactiveException extends AppBaseException {
    public UserInactiveException(ObjectId id) {
        super (Response.Status.CONFLICT, "User with id " + id + " is inactive");
    }
}
