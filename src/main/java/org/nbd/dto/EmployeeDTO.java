package org.nbd.dto;

public record EmployeeDTO(
    String id,
    String login,
    String firstName,
    String lastName,
    String phoneNumber,
    boolean active
) {}
