package org.nbd;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable  ;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Version
    @Column(name = "VERSION")
    private long version;

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}