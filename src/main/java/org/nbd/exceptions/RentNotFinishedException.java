package org.nbd.exceptions;

import org.bson.types.ObjectId;
import jakarta.ws.rs.core.Response;

public class RentNotFinishedException extends AppBaseException {
    public RentNotFinishedException(ObjectId id) {
        super(Response.Status.CONFLICT ,"Can't delete the finished rent with id " + id);
    }
}



