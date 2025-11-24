package org.nbd.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;

public class RentAlreadyEnded extends AppBaseException {
    public RentAlreadyEnded(ObjectId id) {
        super(HttpStatus.CONFLICT, "Rent " + id + " has already been ended!");
    }
}
