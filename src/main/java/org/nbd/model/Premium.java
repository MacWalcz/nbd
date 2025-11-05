package org.nbd.model;


public class Premium extends ClientType {
    @Override
    public double getDiscount() {
        return 0.8;
    }
}
