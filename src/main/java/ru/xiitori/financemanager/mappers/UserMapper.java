package ru.xiitori.financemanager.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.xiitori.financemanager.model.dto.auth.RegistrationDTO;
import ru.xiitori.financemanager.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toEntity(RegistrationDTO dto);

    //UserResponseDTO toDTO(User entity); TODO
}
