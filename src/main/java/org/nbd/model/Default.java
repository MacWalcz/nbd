package org.nbd.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator
public class Default extends ClientType  {
    @Override
    public double getDiscount() {
        return 1;
    }
}
