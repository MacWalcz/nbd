package org.nbd;

public class Premium implements ClientType {
    @Override
    public double getDiscount() {
        return 0.8;
    }
}
