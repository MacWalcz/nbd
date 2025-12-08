package org.nbd.exceptions;

import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

public class HouseActiveRentException extends AppBaseException {
    public HouseActiveRentException(ObjectId houseId) {
        super(Response.Status.CONFLICT, "House " + houseId + " is already rented");
    }
}
