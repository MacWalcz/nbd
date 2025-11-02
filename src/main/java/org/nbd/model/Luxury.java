package org.nbd.model;

public class Luxury implements ClientType{
    @Override
    public double getDiscount() {
        return 0.6;
    }
}
