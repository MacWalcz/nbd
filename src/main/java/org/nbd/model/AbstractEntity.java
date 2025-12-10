package org.nbd.model;

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
@Builder
@AllArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class AbstractEntity implements Serializable {

    @PartitionKey
    private UUID id;

    private long version;
}
