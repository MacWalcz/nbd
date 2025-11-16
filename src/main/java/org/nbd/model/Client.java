package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "clients")
public class Client extends User {

    @DBRef(lazy = false)
    private ClientType clientType;

}
