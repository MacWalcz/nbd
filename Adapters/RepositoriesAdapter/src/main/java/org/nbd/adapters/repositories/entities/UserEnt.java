package org.nbd.adapters.repositories.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "users")
public abstract class UserEnt extends AbstractEnt {
    @NotNull
    @Indexed(unique = true)
    private @NonNull String login;

    @NotNull
    private String password;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    @Builder.Default
    private boolean active = false;
}
