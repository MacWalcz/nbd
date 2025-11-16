package org.nbd.dto;

public record AdministratorDTO(
    String id,
    String login,
    String firstName,
    String lastName,
    String phoneNumber,
    boolean active
) {}
