package org.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity(defaultKeyspace = "rent_a_house")
@CqlName("ClientsIds")
@ToString
@PropertyStrategy(getterStyle = GetterStyle.JAVABEANS)
public class Client extends AbstractEntity {


    @CqlName("firstName")
    private String firstName;
    private String lastName;
    private String phoneNumber;

    private UUID clientTypeId;

    @Builder.Default
    private boolean active = false;
}