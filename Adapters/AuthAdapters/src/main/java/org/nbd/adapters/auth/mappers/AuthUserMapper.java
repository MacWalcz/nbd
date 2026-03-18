package org.nbd.adapters.auth.mappers;


import org.mapstruct.Mapper;
import org.nbd.adapters.auth.model.AuthUser;
import org.nbd.model.User;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    AuthUser toAuthUser(User user);
}
