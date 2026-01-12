package org.nbd.exceptions;

import org.springframework.http.HttpStatus;


public class InvalidClientType extends AppBaseException{
    public InvalidClientType(String id) {
        super(HttpStatus.BAD_REQUEST, "Invalid client type: " + id);
    }
}


