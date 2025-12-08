package org.nbd.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public abstract class AppBaseException extends WebApplicationException {

    protected AppBaseException(Response.Status status, String message) {
        super(Response.status(status).entity(message).build());
    }

    protected AppBaseException(Response.Status status, String message, Throwable cause) {
        super(cause, Response.status(status).entity(message).build());
    }
}
