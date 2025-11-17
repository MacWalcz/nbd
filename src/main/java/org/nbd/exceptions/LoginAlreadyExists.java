package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class LoginAlreadyExists extends AppBaseException {

    public LoginAlreadyExists(String login) {
        super(HttpStatus.CONFLICT, "Login " + login + " already exists");
    }
}
