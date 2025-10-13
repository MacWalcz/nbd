package org.nbd;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "CLIENTS")
@Access(AccessType.FIELD)
public class Client extends AbstractEntity {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(initialValue = 1, name = "clientIdSequence")
    @GeneratedValue(generator = "clientIdSequence")
    private long id;

    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "phonenumber")
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    @JoinColumn(name = "CLIENTTYPE_ID")
    private ClientType clientType;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client(){

    }

    public Client(String firstName, String lastName, String phoneNumber, ClientType clientType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.clientType = clientType;
    }
}
