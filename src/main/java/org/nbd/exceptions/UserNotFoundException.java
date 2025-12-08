package org.nbd.exceptions;

import org.bson.types.ObjectId;
import jakarta.ws.rs.core.Response;

public class UserNotFoundException extends AppBaseException {
    public UserNotFoundException(ObjectId id) {
        super(Response.Status.NOT_FOUND, "Client with id " + id + " not found");
    }
    public UserNotFoundException(String login) {
        super(Response.Status.NOT_FOUND, "Client with login " + login + " not found");
    }
}