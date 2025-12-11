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
import java.time.LocalTime;
import java.util.UUID;



@Entity(defaultKeyspace = "rent_a_house")
@CqlName("rents")
@PropertyStrategy(getterStyle = GetterStyle.JAVABEANS)
public class Rent extends AbstractEntity {

    @NonNull
    @ClusteringColumn
    @CqlName("start_date")
    private LocalDate startDate;
    @CqlName("end_date")
    private LocalDate endDate;

    @CqlName("client_id")
    @NonNull
    private UUID clientId;
    @NonNull
    @CqlName("house_id")
    private UUID houseId;

    private double cost;

    public Rent(UUID id, @NonNull LocalDate startDate, LocalDate endDate, @NonNull UUID clientId, @NonNull UUID houseId, double cost) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.clientId = clientId;
        this.houseId = houseId;
        this.cost = cost;
    }

    public Rent() {
        super();
    }

    public @NonNull LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(@NonNull LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public @NonNull UUID getClientId() {
        return clientId;
    }

    public void setClientId(@NonNull UUID clientId) {
        this.clientId = clientId;
    }

    public @NonNull UUID getHouseId() {
        return houseId;
    }

    public void setHouseId(@NonNull UUID houseId) {
        this.houseId = houseId;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}