package org.nbd.model;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import com.datastax.oss.driver.api.mapper.entity.naming.GetterStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity(defaultKeyspace = "rent_a_house")
@CqlName("RentsIds")
@PropertyStrategy(getterStyle = GetterStyle.JAVABEANS)
public class Rent extends AbstractEntity {

    @NonNull
    @ClusteringColumn
    private LocalDate startDate;
    private LocalDate endDate;

    @NonNull
    private UUID clientId;
    @NonNull
    private UUID houseId;

    private double cost;
}