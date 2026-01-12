package org.nbd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@TypeAlias("employee")
public class Employee extends User {
    private String position;
}