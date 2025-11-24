package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TypeAlias("client")
public class Client extends User {

    @DBRef(lazy = false)
    private ClientType clientType;

}
