package org.nbd.exceptions;

import org.bson.types.ObjectId;
import jakarta.ws.rs.core.Response;


public class RentNotFoundException extends AppBaseException {
    public RentNotFoundException(ObjectId id) {
        super(Response.Status.NOT_FOUND, "Rent with id " + id + " not found");
    }
}