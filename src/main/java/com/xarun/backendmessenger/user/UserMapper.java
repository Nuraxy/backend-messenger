package com.xarun.backendmessenger.user;

import com.xarun.backendmessenger.openapi.model.UserResource;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "registerKey", ignore = true)
    UserResource mapToResource(User user);

    @Mapping(target = "registrationDate", source = "")
    @Mapping(target = "salt", ignore = true)
    User mapToUserDomain(UserResource userResource);
}
