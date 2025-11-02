package org.nbd.model;

public class Default implements ClientType {
    @Override
    public double getDiscount() {
        return 1;
    }
}
