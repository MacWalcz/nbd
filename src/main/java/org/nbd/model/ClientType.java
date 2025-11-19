package org.nbd.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_t")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Default.class, name = "default"),
        @JsonSubTypes.Type(value = Premium.class, name = "premium"),
        @JsonSubTypes.Type(value = Luxury.class, name = "luxury")
})
public abstract class ClientType extends AbstractEntity {
    public abstract double getDiscount();
}
