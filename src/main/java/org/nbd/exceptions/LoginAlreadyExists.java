package org.nbd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class LoginAlreadyExists extends RuntimeException {
    public LoginAlreadyExists(String login) {
        super("Login " + login + " already exists");
    }
}
