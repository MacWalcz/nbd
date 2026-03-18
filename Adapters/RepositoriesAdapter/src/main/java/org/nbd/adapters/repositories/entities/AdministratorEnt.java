package org.nbd.adapters.repositories.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@AllArgsConstructor
@SuperBuilder
@TypeAlias("administrator")
public class AdministratorEnt extends UserEnt { }
