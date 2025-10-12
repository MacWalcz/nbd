package org.nbd;

import jakarta.persistence.*;



@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Access(AccessType.FIELD)
public abstract class ClientType extends AbstractEntity {
    @Id
    @SequenceGenerator(initialValue = 1, name = "clientTypeIdSequence")
    @GeneratedValue(generator = "clientTypeIdSequence")
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public  abstract double getDiscount();

}
