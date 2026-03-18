package org.nbd.adapters.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthUser {
    private ObjectId id;
    private String login;
    private String password;
}
