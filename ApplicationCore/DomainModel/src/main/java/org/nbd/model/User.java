package org.nbd.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@SuperBuilder
public abstract class User extends AbstractEntity {

    private @NonNull String login;


    private @NonNull String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Builder.Default
    private boolean active = false;
}
