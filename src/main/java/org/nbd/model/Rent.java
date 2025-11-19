package org.nbd.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Rent extends AbstractEntity {

    @NonNull
    private LocalDate startDate;
    private LocalDate endDate;

    @NonNull
    private Client client;
    @NonNull
    private House house;

    private double cost;
}
