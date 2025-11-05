package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends User {
    private String firstName;
    private String lastName;
    private String phoneNumeber;
    @DBRef(lazy = false)
    private ClientType clientType;

    @Builder.Default
    private boolean active = false;
}
