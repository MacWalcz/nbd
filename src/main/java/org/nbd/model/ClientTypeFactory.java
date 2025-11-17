package org.nbd.model;

import org.nbd.exceptions.InvalidClientType;

public class ClientTypeFactory {
    public static ClientType create(String which) {
        ClientType ct = null;
        switch (which) {
            case "1":
                ct = new Default();
                ct.setId("1");
                return ct;

            case "2":
                ct = new Premium();
                ct.setId("2");
                return ct;

            case "3":
                ct = new Luxury();
                ct.setId("3");
                return new Luxury();

            default:
                throw new InvalidClientType(which);
        }
    }
}
