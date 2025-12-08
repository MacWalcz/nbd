package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.types.ObjectId;

@BsonDiscriminator
@NoArgsConstructor(force = true)
@SuperBuilder
@Data
public abstract class User extends AbstractEntity {
    private String login;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Builder.Default
    private boolean active = false;
}
