package com.xarun.backendmessenger.token;

import com.xarun.backendmessenger.openapi.model.TokenResource;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TokenMapper {

    @Mapping(target = "user.password", ignore = true)
    TokenResource mapToResource(Token token);

}
