package org.nbd.dto;

import org.nbd.model.ClientType;

import java.util.UUID;

public record ClientDTO(String id, String login, String firstName, String lastName, String phoneNumber, ClientType clientType) {

    public ClientDTO(String id, String login, String firstName, String lastName, String phoneNumber, ClientType clientType) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClientDTO{");
        sb.append("id=").append(id);
        sb.append(", login='").append(login).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", phoneNumber='").append(phoneNumber).append('\'');
        sb.append(", clientType=").append(clientType);
        sb.append('}');
        return sb.toString();
    }


    public String id() {
        return id;
    }


    public String login() {
        return login;
    }


    public String firstName() {
        return firstName;
    }


    public String lastName() {
        return lastName;
    }


    public String phoneNumber() {
        return phoneNumber;
    }


    public ClientType clientType() {
        return clientType;
    }
}
