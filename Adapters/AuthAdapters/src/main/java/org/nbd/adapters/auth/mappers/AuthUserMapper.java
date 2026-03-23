package org.nbd.adapters.auth.mappers;



import org.nbd.adapters.auth.model.AuthUser;
import org.nbd.model.User;


public class AuthUserMapper {
    public static AuthUser toAuthUser(User user){
        return AuthUser.builder()
                .id(user.getId())
                .login(user.getLogin())
                .role(user.getClass().toString().toUpperCase())
                .build();

    }

}
