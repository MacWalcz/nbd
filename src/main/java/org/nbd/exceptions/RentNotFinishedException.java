package org.nbd.exceptions;

import org.springframework.http.HttpStatus;


public class RentNotFinishedException extends AppBaseException {
    public RentNotFinishedException(String id) {
        super(HttpStatus.CONFLICT ,"Can't delete the finished rent with id " + id);
    }
}



