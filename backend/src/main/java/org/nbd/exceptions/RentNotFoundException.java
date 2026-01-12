package org.nbd.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;


public class RentNotFoundException extends AppBaseException {
    public RentNotFoundException(ObjectId id) {
        super(HttpStatus.NOT_FOUND, "Rent with id " + id + " not found");
    }
}