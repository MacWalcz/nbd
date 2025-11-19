package org.nbd.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator
public abstract class ClientType extends AbstractEntity {
    public abstract double getDiscount();
}
