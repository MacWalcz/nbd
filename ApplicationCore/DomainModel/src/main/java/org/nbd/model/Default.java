package org.nbd.model;




public class Default extends ClientType  {
    @Override
    public double getDiscount() {
        return 1;
    }

    @Override
    public String toString() {
        return "1";
    }


}
