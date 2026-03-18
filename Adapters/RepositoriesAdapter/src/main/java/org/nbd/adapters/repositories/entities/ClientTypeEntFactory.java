package org.nbd.adapters.repositories.entities;

import org.apache.logging.log4j.util.InternalException;
import org.bson.types.ObjectId;


public class ClientTypeEntFactory {
    public static ClientTypeEnt create(String which) {
        ClientTypeEnt ct = null;
        switch (which) {
            case "1":
                ct = new DefaultEnt();
                ct.setId(new ObjectId("000000000000000000000001"));
                return ct;

            case "2":
                ct = new PremiumEnt();
                ct.setId(new ObjectId("000000000000000000000002"));
                return ct;

            case "3":
                ct = new LuxuryEnt();
                ct.setId(new ObjectId("000000000000000000000003"));
                return ct;

            default:
                throw new InternalException(which);
        }
    }
}
