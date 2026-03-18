package org.nbd.adapters.repositories.entities;


public class PremiumEnt extends ClientTypeEnt {
    @Override
    public double getDiscount() {
        return 0.8;
    }

    @Override
    public String toString() {
        return "2";
    }

}
