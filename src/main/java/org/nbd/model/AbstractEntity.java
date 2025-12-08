package org.nbd.model;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.nbd.config.ObjectIdAdapter;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractEntity implements Serializable {
    @BsonId                     // dla MongoDB
    @JsonbTransient                // ukrywamy oryginalne pole ObjectId
    private ObjectId id;

    // to pole będzie widoczne w JSON-ie jako czysty string
    @JsonbProperty("_id")
    public String getId() {
        return id != null ? id.toHexString() : null;
    }

    public void setId(String id) {
        this.id = id != null ? new ObjectId(id) : null;
    }

    private long version;
}
