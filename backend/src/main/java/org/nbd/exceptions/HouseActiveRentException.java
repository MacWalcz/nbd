package org.nbd.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;


public class HouseActiveRentException extends AppBaseException {
    public HouseActiveRentException(ObjectId houseId) {
        super(HttpStatus.CONFLICT, "House is already rented");
    }
}

