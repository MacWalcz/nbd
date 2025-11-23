package org.nbd.model;

import org.bson.types.ObjectId;
import org.nbd.exceptions.InvalidClientType;

public class ClientTypeFactory {
    public static ClientType create(String which) {
        ClientType ct = null;
        switch (which) {
            case "1":
                ct = new Default();
                ct.setId(new ObjectId("000000000000000000000001"));
                return ct;

            case "2":
                ct = new Premium();
                ct.setId(new ObjectId("000000000000000000000002"));
                return ct;

            case "3":
                ct = new Luxury();
                ct.setId(new ObjectId("000000000000000000000003"));
                return new Luxury();

            default:
                throw new InvalidClientType(which);
        }
    }
}
