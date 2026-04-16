package ru.xiitori.financemanager.mappers;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.xiitori.financemanager.model.dto.auth.RegistrationDTO;
import ru.xiitori.financemanager.model.dto.permission.PermissionResponseDTO;
import ru.xiitori.financemanager.model.dto.user.UserResponseDTO;
import ru.xiitori.financemanager.model.entity.Permission;
import ru.xiitori.financemanager.model.entity.User;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    User toEntity(RegistrationDTO dto);

    @Mapping(target = "permissions", ignore = true)
    UserResponseDTO toDto(User entity);

    @AfterMapping
    default void mapPermissions(
            User entity,
            @MappingTarget UserResponseDTO dto
    ) {
        var permissions = entity.getUserPermissions().stream()
                .map(p ->
                        mapToPermissionDTO(p.getPermission()))
                .collect(Collectors.toSet());

        dto.setPermissions(permissions);
    }

    PermissionResponseDTO mapToPermissionDTO(Permission permission);
}
