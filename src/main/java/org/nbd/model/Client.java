package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "clients")
public class Client extends User {
    private String firstName;
    private String lastName;
    private String phoneNumeber;
    private ClientType clientType;

    @Builder.Default
    private boolean active = false;
}
