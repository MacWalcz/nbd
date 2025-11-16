package org.nbd.dto;

public record HouseDTO (
    String id,
    String houseNumber,
    double price,
    double area
) {}
