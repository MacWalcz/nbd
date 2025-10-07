package org.nbd;

public class Client {
    private String personalId;
    private String firstName;
    private String lastName;
    private String phoneNumeber;
    private ClientType clientType;

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

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

    public String getPhoneNumeber() {
        return phoneNumeber;
    }

    public void setPhoneNumeber(String phoneNumeber) {
        this.phoneNumeber = phoneNumeber;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public Client(String personalId, String firstName, String lastName, String phoneNumeber, ClientType clientType) {
        this.personalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumeber = phoneNumeber;
        this.clientType = clientType;
    }
}
