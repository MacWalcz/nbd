package org.nbd;

public class Luxury implements ClientType{
    @Override
    public double getDiscount() {
        return 0.6;
    }
}
