package ru.xiitori.financemanager.model.dto.user;

import lombok.Data;
import ru.xiitori.financemanager.model.dto.permission.PermissionResponseDTO;

import java.util.Set;

@Data
public class UserResponseDTO {
    Long id;
    String username;
    Set<PermissionResponseDTO> permissions;
    boolean enabled;
    boolean locked;
}
