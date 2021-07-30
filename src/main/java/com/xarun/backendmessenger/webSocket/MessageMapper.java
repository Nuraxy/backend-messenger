package com.xarun.backendmessenger.webSocket;

import com.xarun.backendmessenger.openapi.model.MessageResource;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MessageMapper {

    MessageResource mapToResource(Message message);

    Message mapToMessageDomain(MessageResource messageResource);
}
