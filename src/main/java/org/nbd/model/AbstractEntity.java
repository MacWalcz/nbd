package org.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@ToString
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class AbstractEntity implements Serializable {
    @PartitionKey
    @CqlName("id")
    private UUID id;
}