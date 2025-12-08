package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractEntity implements Serializable {
    private ObjectId id;

    private long version;
}
