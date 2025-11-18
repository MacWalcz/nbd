package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@SuperBuilder
@RequiredArgsConstructor
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
