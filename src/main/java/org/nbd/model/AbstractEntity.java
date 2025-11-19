package org.nbd.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class AbstractEntity implements Serializable {
    @BsonId
    private ObjectId id;

    private long version;
}
