package org.nbd.model;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Client extends User {
    private String firstName;
    private String lastName;
    private String phoneNumeber;
    private ClientType clientType;

    @Builder.Default
    private boolean active = false;


}
