package org.nbd.dto;

import org.nbd.model.Client;
import org.nbd.model.House;

import java.time.LocalDate;

public record RentDTO(
    String id,
    LocalDate startDate,
    LocalDate endDate,

    Client client,
    House house
) {}
