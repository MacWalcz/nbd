package org.nbd.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.validation.constraints.*;
import org.bson.types.ObjectId;

public class EmployeeDTO {

    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    @NotBlank(message = "Login cannot be blank")
    @Size(min = 3, max = 30, message = "Login must be between 3 and 30 characters")
    private String login;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Size(min = 7, max = 15, message = "Login must be between 3 and 30 characters")
    private String phoneNumber;

    private Boolean active;

    public EmployeeDTO() {}

    public EmployeeDTO(String id, String login, String firstName, String lastName,
                       String phoneNumber, Boolean active) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.active = active;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
