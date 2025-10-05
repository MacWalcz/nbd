package org.nbd;

public class Default implements ClientType {
    @Override
    public double getDiscount() {
        return 1;
    }
}
