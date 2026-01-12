package org.nbd.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;

@Data
@AllArgsConstructor
@SuperBuilder
@TypeAlias("administrator")
public class Administrator extends User { }
