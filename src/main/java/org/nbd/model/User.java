package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class User extends AbstractEntity {
    private String login;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Builder.Default
    private boolean active = false;
}
