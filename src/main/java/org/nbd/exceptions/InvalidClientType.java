package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class InvalidClientType extends AppBaseException{
    public InvalidClientType(String id) {
        super(HttpStatus.BAD_REQUEST, "Invalid client type: " + id);
    }
}


