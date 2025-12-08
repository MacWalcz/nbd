package org.nbd.exceptions;

import org.bson.types.ObjectId;
import jakarta.ws.rs.core.Response;

public class RentAlreadyEnded extends AppBaseException {
    public RentAlreadyEnded(ObjectId id) {
        super(Response.Status.CONFLICT, "Rent " + id + " has already been ended!");
    }
}
