package org.nbd.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;

public class HouseNotAvaibleException extends AppBaseException {
    public HouseNotAvaibleException(ObjectId id) {
        super(HttpStatus.CONFLICT, "House " + id + " is not available.");
    }
}
