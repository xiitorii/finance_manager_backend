package ru.xiitori.financemanager.mappers;

import org.mapstruct.Mapper;
import ru.xiitori.financemanager.model.dto.permission.PermissionResponseDTO;
import ru.xiitori.financemanager.model.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponseDTO toDto(Permission permission);
}
