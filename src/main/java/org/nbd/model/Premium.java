package org.nbd.model;

public class Premium implements ClientType {
    @Override
    public double getDiscount() {
        return 0.8;
    }
}
