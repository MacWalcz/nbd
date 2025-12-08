package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key="_class", value="employee")
@NoArgsConstructor
@SuperBuilder
@Data
public class Employee extends User { }
