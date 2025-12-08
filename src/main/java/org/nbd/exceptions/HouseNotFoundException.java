package org.nbd.exceptions;

import org.bson.types.ObjectId;
import jakarta.ws.rs.core.Response;

public class HouseNotFoundException extends AppBaseException {
    public HouseNotFoundException(ObjectId id) {
        super(Response.Status.NOT_FOUND,"House with id " + id.toHexString() + " not found");
    }
}