package org.nbd.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;

@BsonDiscriminator(key="_class", value="administrator")
@NoArgsConstructor
@SuperBuilder
@Data
public class Administrator extends User { }
