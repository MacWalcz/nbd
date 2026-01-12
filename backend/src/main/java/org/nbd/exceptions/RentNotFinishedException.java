package org.nbd.exceptions;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;


public class RentNotFinishedException extends AppBaseException {
    public RentNotFinishedException(ObjectId id) {
        super(HttpStatus.CONFLICT ,"Can't delete the finished rent with id " + id);
    }
}



