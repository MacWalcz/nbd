package org.nbd.exceptions;

import org.bson.types.ObjectId;
import jakarta.ws.rs.core.Response;

public class InvalidClientType extends AppBaseException{
    public InvalidClientType(String id) {
        super(Response.Status.BAD_REQUEST, "Invalid client type: " + id);
    }
}


