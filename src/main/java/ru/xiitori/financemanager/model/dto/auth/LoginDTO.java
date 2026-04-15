package ru.xiitori.financemanager.model.dto.auth;

import jakarta.validation.constraints.NotNull;

public record LoginDTO(
        @NotNull
        String username,
        @NotNull
        String password
) {
}
