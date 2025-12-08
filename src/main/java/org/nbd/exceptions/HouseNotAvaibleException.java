package org.nbd.exceptions;

import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

public class HouseNotAvaibleException extends AppBaseException {
    public HouseNotAvaibleException(ObjectId id) {
        super(Response.Status.CONFLICT, "House " + id + " is not available.");
    }
}
