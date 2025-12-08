package org.nbd.exceptions;

import jakarta.ws.rs.core.Response;

public class LoginAlreadyExists extends AppBaseException {
    public LoginAlreadyExists(String login) {
        super(Response.Status.CONFLICT, "Login " + login + " already exists");
    }
}
