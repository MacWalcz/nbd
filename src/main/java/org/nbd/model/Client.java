package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends AbstractEntity {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    //@DBRef(lazy = false)
    private ClientType clientType;

    @Builder.Default
    private boolean active = false;
}
