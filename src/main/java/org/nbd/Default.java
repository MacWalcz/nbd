package org.nbd;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;


@Entity
@DiscriminatorValue("def")
public class Default extends ClientType {
    @Override
    public double getDiscount() {
        return 1;
    }
}
