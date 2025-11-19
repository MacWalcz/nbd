package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Client extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private ClientType clientType;

    @Builder.Default
    private boolean active = false;
}
