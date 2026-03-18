package org.nbd.adapters.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@TypeAlias("clientEnt")
public class ClientEnt extends UserEnt {

    @DBRef(lazy = false)
    private ClientTypeEnt clientTypeEnt;

}
